#################################################
#
# chowhound.py - Script for scraping Chowhound
#
#################################################
# This script scrapes Chowhound's message boards
# and stores the data in a TreeML XML file for 
# use in Overherd. It is the script on which 
# scripts for other discussion board sources will
# be based.
# 
# OUTPUT: chowhound_chicago.xml
#
# e.g. The Chicago board at http://chowhound.chow.com/boards/7
#################################################

#################################################
# Chowhound's structure
#################################################
# Script should have to deal with only three types
# of page - main, board, topic
# main - lists all boards available
# board - front page for a board, e.g., Chicago Area
# topic - detail view of a topic including all replies
#
# Script should scrape compassionately - cache
# often, stagger requests and make as few as possible
# Because topic pages include all replies, we should
# have to http request content from Chowhound only
# when grabbing a new board or topic 

#################################################
# Format of the resulting XML file - written by Srikkanth Sundararajan
#################################################
# <Tree>
#   <declarations>
# 	<!-- Here goes all the attribute declarations (such as name,type of the attributes involved) -->
#   </declarations	
# <branch>
# 	<branch>
# 		<!-- FORUM INFO -->
#		(forum id , forum title,forum short,forum extended etc)
#
# 		<branch>
#			<!-- TOPIC INFO -->
#			(forum_id, topic id , topic title,topic short , extended ,length etc)
#
#			<branch>
#				<!-- MESSAGE INFO -->
#				(message id,topic_id,forum_id,message body,message date,message parent etc)
#			</branch>
#
#		<branch>
#	</branch>
#
#</branch>
#</Tree>
#################################################

# TreeML label = Chowhound label
# Forum = board
# Topic = topic
# Message = message

import httplib2,time,re
from BeautifulSoup import BeautifulSoup
SCRAPING_CONN = httplib2.Http(".cache")
SCRAPING_DOMAIN_RE = re.compile("\w+:/*(?P<domain>[a-zA-Z0-9.]*)/")
SCRAPING_DOMAINS = {}
SCRAPING_CACHE_FOR = 60 * 15 # cache for 15 minutes
SCRAPING_REQUEST_STAGGER = 1100 # in milliseconds
SCRAPING_CACHE = {}

# other variables
# topic counter
# message counter
# board id

# get lists of boards from Chowhound, store the list 
# OUTPUT: file listing boards, for use in user select

# INPUT: user selects board to scrape from list, returns "board id"

####### start writing to xml
# open chowhound.xml for writing
# write to <Tree>
#	write to <declarations>
# close <declarations>
# loop through topics gathering id, title, length
# 	OUTPUT: print "[topic title] Started scraping" to console
# 	write to <branch>
# 	loop through messages within topic gathering id, body, date, parent
#		write to <branch>
#		close <branch>
# 	close message loop
#	close <branch>
# 	OUTPUT: print "[topic title] Finished scraping X messages" to console
# close topic loop
# increase topic count
# close <Tree>
# save and close chowhound.xml
# OUTPUT: chowhound.xml
######## finish writing to xml
# 

# INPUT: ask user if s/he would like to scrape another board
# If yes, repeat from "start writing to xml" to "ask user if s/he would like to scrape another board"
# If no, exit


