
# File: CsvToTreeML.rb
# Author: <a href="http://kevinnam.com> Kevin Nam </a>
# Purpose: Creates a TreeML file from the forum CSVs.  A TreeML is an XML format for a tree data structure.
#          This is necessary for the Prefuse toolkit.
# Version 0.1

require 'rubygems'
require 'faster_csv'
require 'rexml/document'
require 'add-ons/strip_html'
include REXML

#input file names
forums_filename = "D:/Documents/Sakai/treemap/forumviz/forumviz/data/forum_data/forums.csv"
topics_filename = "D:/Documents/Sakai/treemap/forumviz/forumviz/data/forum_data/topics.csv"
messages_filename = "D:/Documents/Sakai/treemap/forumviz/forumviz/data/forum_data/messages.csv"

#output file name
out_file="forumTree.xml"

# dump the CSV files into arrays
forums_array=FasterCSV.read(forums_filename, :headers => true)
topics_array=FasterCSV.read(topics_filename, :headers => true)
messages_array=FasterCSV.read(messages_filename, :headers => true)

#although it looks like messages.csv has messages grouped together properly, can't count on that.  
#build a proper xml tree.
#add top levels first to keep it simple.
#I am declaring all the ids of type String snice I don't know how big they can get. If I know the upper
# limit, I can change them to Integer or something else.
doc=Document.new
doc.add_element("tree")
doc.root.add_element("declarations")
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"name","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_title", "type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_id", "type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_short","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_extended","type"=>"String"})
#doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_date","type"=>"Timestamp"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"forum_length","type"=>"Integer"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_id","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_title","type"=>"String"})
#doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_date","type"=>"Timestamp"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_creator","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_short","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_extended","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"topic_length","type"=>"Integer"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"message_id","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"message_title","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"message_author","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"message_body","type"=>"String"})
doc.root.elements["declarations"].add_element("attributeDecl", {"name"=>"message_length","type"=>"Integer"})

branch1=doc.root.add_element("branch")

#add forums
forums_array.each{|forum|
 branch2=branch1.add_element("branch") #,"forum_title"=>forum[2])
 branch2.add_element("attribute", "name"=>"name","value"=>forum[2])
 branch2.add_element("attribute","name"=>"forum_title", "value"=>forum[2])
 branch2.add_element("attribute","name"=>"forum_id","value"=>forum[1])
# branch2.add_element("attribute","forum_date"=>forum[3])
 branch2.add_element("attribute","name"=>"forum_short", "value"=>forum[5])
 temp_extended=forum[6]
 if temp_extended==nil
   temp_extended=""
 else
   temp_extended=forum[6].strip_html
 end
# puts temp_extended
# branch2.add_element("attribute","forum_extended"=>temp_extended)
}


#add topics
topics_array.each {|topic|
  forum_id=topic[1]
  branch3=doc.root.elements["//attribute[@value=" + forum_id + "]"]
  
  p=branch3.parent
  branch4=p.add_element("branch")
  branch4.add_element("attribute","name"=>"name", "value"=>topic[3])
  branch4.add_element("attribute","name"=>"topic_title", "value"=>topic[3])
  branch4.add_element("attribute","name"=>"topic_id","value"=>topic[2])
  branch4.add_element("attribute","name"=>"topic_short","value"=>topic[6])
  
}

added=[]
not_inserted=[]
countline=0

#I know this is messy and bad, but for now..
#the way the messages csv files list its posts information is that the parent post that
#the current post replied to may not have been encountered at the time of the current post
#Since you can add a post to a tree whose parent you don't know yet, I set it aside and check for it
#next time when a new post is added to the tree

#changes to make: create an array of added node and check for there, rather than checking the tree
#should make it faster
#use added.include?(nodeid)

messages_array.each{ |message|
  puts "doing message " + message[3]
  reply_to=message[8]
  not_inserted_this_time=false
  if(reply_to==nil)
    #if reply_to is empty, means top node
    topic_id=message[2]
    branch=doc.root.elements["//attribute[@value=" + topic_id+"]"]
    branch2=branch.add_element("branch")
    branch2.add_element("attribute","name"=>"name","value"=>message[5])
    branch2.add_element("attribute","name"=>"message_id","value"=>message[4])
  else
    #if no parent found, add into not_inserted
    branch=doc.root.elements["//attribute[@value="+reply_to+"]"]
    if(branch==nil)
      #parent not found
      puts "parent not found.  Added." + countline.to_s()
      not_inserted.push(countline)
      not_inserted_this_time=true
    else
      puts "parent found.  Adding..."
      branch2=branch.add_element("branch")
      branch2.add_element("attribute","name"=>"name","value"=>message[5])
      branch2.add_element("attribute","name"=>"message_id","value"=>message[4])
    end
    
    num_to_check=not_inserted.length-1
    puts "not_inserted len: " + num_to_check.to_s()
    checked_count=0
    if(not_inserted_this_time and not_inserted.length>1)
      while(not_inserted.length>1 and checked_count<num_to_check)
        puts "checked_count: " + checked_count.to_s
        checked_count+=1
        
       
        linenum=not_inserted.shift
        reply_to=messages_array[linenum][8]
        puts linenum.to_s() + ": checks for newly found parent... " + reply_to
        
         #if no parent found, add into not_inserted
        branch=doc.root.elements["//attribute[@value="+reply_to+"]"]
        if(branch==nil)
          #parent not found
          not_inserted.push(linenum)
        else
          puts "Parent discovered later!!!!!!!!!!!!!!!!!!!!!!!"
          branch2=branch.add_element("branch")
          branch2.add_element("attribute","name"=>"name","value"=>message[5])
          branch2.add_element("attribute","name"=>"message_id","value"=>message[4])
        end
      end
    
    end
  end
  countline+=1
}

#check if there's anything left in the not_inserted.  There shouldn't be.
if(not_inserted.length>0)
  puts "One or more nodes without a parent have been found!  Their lines are:"
  not_inserted.each{|num|
    puts num 
  }
end

#add posts


out=File.open(out_file, 'w')
out<<doc
out.close

