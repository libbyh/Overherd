#! /usr/bin/env python
# chowhound.py - scrapes Chowhound discussion board to create XML
# script requires Python 2.6

import httplib2, time, re, htmlentitydefs
from BeautifulSoup import BeautifulSoup
from elementtree.ElementTree import Element, SubElement, ElementTree

SCRAPING_CONN = httplib2.Http(".cache")
SCRAPING_DOMAIN_RE = re.compile("\w+:/*(?P<domain>[a-zA-Z0-9.]*)/")
SCRAPING_DOMAINS = {}
SCRAPING_CACHE_FOR = 60 * 15
SCRAPING_REQUEST_STAGGER = 1100
SCRAPING_CACHE = {} 

ftime=[]

def fetch(url,method = "GET"):
    global ftime #testing
    start = time.time() #testing
    ''' fetch url, if it is in cache:get html, if not:request html'''
    key = (url, method)
    now = time.time()
    if SCRAPING_CACHE.has_key(key):
        data, cached_at = SCRAPING_CACHE[key]
        if now - cached_at < SCRAPING_CACHE_FOR:
            return data
    domain = SCRAPING_DOMAIN_RE.findall(url)[0]
    if SCRAPING_DOMAINS.has_key(domain):
        last_scraped = SCRAPING_DOMAINS[domain]
        elapsed = now - last_scraped
        if elapsed < SCRAPING_REQUEST_STAGGER:
            wait_period = (SCRAPING_REQUEST_STAGGER - elapsed) / 1000
            time.sleep(wait_period)
    SCRAPING_DOMAINS[domain] = time.time()
    data = SCRAPING_CONN.request(url, method)
    SCRAPING_CACHE[key] = (data, now)
    end = time.time() #testing
    ftime.append(end-start) #testing
    return data

def goodString(input):
    """ some times "author" have addition non-letter & invisible characters.
    this fuction will filter the undisired characters out.
    """
    string = str(input)
    i = 0
    j = len(string)-1
    while(True):
        if string[i].isalpha() or string[i].isdigit():
            break
        i=i+1
    while(True):
        if string[j].isalpha() or string[j].isdigit():
            break
        j=j-1
    return string[i:j+1]

def goodDigit(input):
    string = str(input)
    i = 0
    j = len(string)-1
    while(True):
        if string[i].isdigit():
            break
        i=i+1
    while(True):
        if string[j].isdigit():
            break
        j=j-1
    return string[i:j+1]

def getPostContent(soup): # post_soup ok, good_reply soup ok.
    post = soup.find('div', 'post_body')
    #content_list = post.findAll()
    content_list = post.contents
    content = ""
    for item in content_list:
        content = content + str(item)
    content = content.replace("<p>", " ")
    content = content.replace("</p>", " ")
    content = content.replace("<br />", "\n")
    return fixup(content)

def fixup(text):
    """ html entities to string """
    fixed = BeautifulSoup(text, convertEntities = \
            BeautifulSoup.HTML_ENTITIES).contents[0]
    return fixed
    
def getADT(soup): #soup is good reply
    """ input a soup , return author date time,
    A author, D data, T time
    """
    adt = soup.findAll('p', 'vcard author', limit = 1)[0]
    if len(adt.strong.contents) == 1:
        author = str(adt.strong.contents[0].string)
    else:
        author = str(adt.strong.contents[1].string)
    # above, author done 
    datetime = goodString( adt.strong.nextSibling.string[5:] )
    date = datetime[8:12]+"-"+datetime[0:3]+"-"+datetime[4:6]
    the_string = datetime[10:12] + " " + datetime[0:3] + " " + datetime[4:6]
    # the_string = "YY MMM DD"
    the_date_time = time.strptime(the_string, "%y %b %d")
    epoch_seconds = str(int(time.mktime(the_date_time)))
    month_dic = {"Jan":"01", "Feb":"02", "Mar":"03", "Apr":"04", \
            "May":"05", "Jun":"06", "Jul":"07", "Aug":"08", "Sep":"09", \
            "Oct":"10", "Nov":"11", "Dec":"12"}
    for k,v in month_dic.iteritems():
        date = date.replace(k, v)
    # above replace month with nuerical value, date done
    hh = datetime[13:15]
    if datetime[-2:] == "PM" and hh != "12":
        hh = str(int(hh)+12)
    mmss = ":"+datetime[16:18]+":00"
    the_time = hh + mmss
    the_datetime = date + " " + the_time
    return author, the_datetime, epoch_seconds

def getID(soup):
    idString = soup.findAll('div', 'post_body', limit = 1)[0].attrs[1][1]
    the_id = goodDigit(idString)
    return the_id

def getTitle(soup):
    """ only root_post could use this method"""
    title = soup.findAll('h1', 'title entry-title', limit = 1)[0].string
    return fixup(goodString(title))
    
atime = []

topic_soup_e = None
def add(parent_e, post_soup, level = 1): 
    """ parent_post is a Element, post is Soup instance 
    level 0: topic  
    level 1: Default reply(message)
    """
    start = time.time() #testing
    
    post_soup_e = SubElement(parent_e, "branch")
    
    global topic_soup_e 
    if level == 0: #post
        topic_soup_e = post_soup_e
        title = getTitle(post_soup)
        """    
        SubElement(post_soup_e, "attribute", name = "topic_title", value = title)
        SubElement(post_soup_e, "attribute", name = "name", value = title)
        the_type = "topic"
        the_content ="topic_extended"
        """
        #SubElement(post_soup_e, "attribute", name = "message_title", value = title)
        SubElement(post_soup_e, "attribute", name = "name", value = title)
        the_type = "message"
        the_content ="message_body"

    else:
        #SubElement(post_soup_e, "attribute", name = "message_title", value = "message no title")
        SubElement(post_soup_e, "attribute", name = "name", value = "message not name")
        the_type = "message"
        the_content ="message_body"
    
    SubElement(post_soup_e, "attribute", name = "type", value = the_type)
    
    the_id = getID(post_soup)
    SubElement(post_soup_e, "attribute", name = the_type + "_id", value = the_id)
    
    (author, date_time, epoch_seconds) = getADT(post_soup)
    SubElement(post_soup_e, "attribute", name = "author", value = author)
    SubElement(post_soup_e, "attribute", name = the_type + "_date", value = date_time)
    #SubElement(post_soup_e, "attribute", name = "time", value = post_time)
    content = getPostContent(post_soup)
    SubElement(post_soup_e, "attribute", name = the_content, value = content)

    #SubElement(post_soup_e, "attribute", name = "chars", value = str(len(content)))
    word_count = len(content.split(" "))
    #SubElement(post_soup_e, "attribute", name = "words", value = str(word_count))   
    SubElement(post_soup_e, "attribute", name = "epoch_seconds", value = epoch_seconds)

    num,sub_soup_list =  haveReply(post_soup, num = 2)  
    if num !=0:
        sub_soup = sub_soup_list[0]
        num = len( sub_soup.findNextSiblings('li') )
        for i in range(0, num + 1):
            add(topic_soup_e, sub_soup)
            if len(sub_soup.findNextSiblings('li', limit = 2)) != 0:
                sub_soup = sub_soup.findNextSiblings('li', limit = 2)[0]

rtime = [] #testing

def haveReply(soup, num = 1): # only good_reply soup ok
    start = time.time() #testing
    replies_raw = [tag for tag in soup.findAll('li')]
    replies = []   
    for i in range(0, len(replies_raw)):
        if replies_raw[i].ol != None:
            replies.append(replies_raw[i])
        
    end = time.time() #testing
    global rtime
    rtime.append(end - start)
    return len(replies), replies

def main():
    urls=[]
    all_post_soup_list = []      

    print "which board you want to scrape(input a number)?"
    board_number = unicode((raw_input()))
    url = u"http://chowhound.chow.com/boards/"
    url = url + board_number
    print "testing?(y/n)"
    testing = raw_input()
    
    first_topic = 0 
    if testing == "y":
        first_topic = 45
    
    start = time.time()
    page = fetch(url,"GET")
    soup = BeautifulSoup(page[1])
    
    firstPart = soup.findAll("td","post_title")
    for item in firstPart:
        urls.append(unicode(item.findChildren()[1]['href']))
  
    tree_name = "tree" + board_number + ".xml" 
    file = open(tree_name, "w")
    overherd = Element("tree")
    dec = SubElement(overherd, "declarations")
    
    SubElement(dec, "attributeDecl", name = "name", type = "String")
    SubElement(dec, "attributeDecl", name = "forum_title", type = "String")
    SubElement(dec, "attributeDecl", name = "forum_short", type = "String")
    SubElement(dec, "attributeDecl", name = "topic_title", type = "String")
    SubElement(dec, "attributeDecl", name = "topic_short", type = "String")
    SubElement(dec, "attributeDecl", name = "topic_id", type = "String")
    SubElement(dec, "attributeDecl", name = "topic_date", type = "String")
    SubElement(dec, "attributeDecl", name = "topic_extended", type = "String")
    SubElement(dec, "attributeDecl", name = "author", type = "String")
    SubElement(dec, "attributeDecl", name = "time", type = "String")
    SubElement(dec, "attributeDecl", name = "message_title", type = "String")
    SubElement(dec, "attributeDecl", name = "message_id", type = "String")
    SubElement(dec, "attributeDecl", name = "message_body", type = "String")
    SubElement(dec, "attributeDecl", name = "message_date", type = "String")
    SubElement(dec, "attributeDecl", name = "chars", type = "String")
    SubElement(dec, "attributeDecl", name = "words", type = "String") 
    #SubElement(dec, "attributeDecl", name = "forum", type = "String") 
    SubElement(dec, "attributeDecl", name = "type", type = "String") 
    SubElement(dec, "attributeDecl", name = "epoch_seconds", type = "Long") 
    
    branch = SubElement(overherd, "branch")
    
    forum = SubElement(branch, "branch")
    SubElement(forum, "attribute", name = "name", value = "Chowhound/Chicago name")
    SubElement(forum, "attribute", name = "forum_title", value = "Chowhound/Chicago title")
    SubElement(forum, "attribute", name = "forum_short", value = "Chowhound/Chicago short")
    SubElement(forum, "attribute", name = "type", value = "forum")
    SubElement(forum, "attribute", name = "author", value = "Chowhound Author")
    SubElement(forum, "attribute", name = "epoch_seconds", value = "1226983822")
    
   
    for i in range(first_topic, len(urls)):
        page = fetch(urls[i], "GET")
        soup = BeautifulSoup(page[1])
        add(forum, soup, 0)

    ElementTree(overherd).write(file)

    end = time.time()
    print "total time ",end-start
    ''' 
    global ftime
    sum1 = 0
    for item in ftime:
        sum1 = sum1 + item
    print "fetch time ",sum1    
    global rtime
    sum1 = 0
    for item in rtime:
        sum1 = sum1 + item
    print "haveReply time ",sum1
    sum1=0
    for item in atime:
        sum1 = sum1 +item
    print "add time ",sum1
    ''' 
    file.close()
    
    
if __name__ == "__main__":
    main()
