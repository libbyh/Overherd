
# File: CsvToTreeML.rb
# Author: <a href="http://kevinnam.com> Kevin Nam </a>
# Purpose: Creates a TreeML file from the forum CSVs.  A TreeML is an XML format for a tree data structure.
#          This is necessary for the Prefuse toolkit.
# Version 0.2 - fixed a few bugs and is now a bit faster

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
out_file="forumTree2.xml"

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

#added=[]
not_inserted=[]
countline=0

#I know this is messy and bad, but for now..
#the way the messages csv files list its posts information is that the parent post that
#the current post replied to may not have been encountered at the time of the current post
#Since you can add a post to a tree whose parent you don't know yet, I set it aside and check for it
#next time when a new post is added to the tree


messages_array.each{ |message|
  puts "-- doing line " + countline.to_s() + ", message " + message[3]
  reply_to=message[8]
  not_inserted_this_time=false
  topic_id=message[2]
  if(reply_to==nil)
    #if reply_to is empty, means top node
    puts "top node " + message[4].to_s() + " found.  Being added."
    branch=doc.root.elements["//attribute[@value=" + topic_id+"]"]
    branch2=branch.add_element("branch")
    branch2.add_element("attribute","name"=>"name","value"=>message[5])
    branch2.add_element("attribute","name"=>"message_id","value"=>message[4])
    branch2.add_element("attribute","name"=>"message_body","value"=>message[11])
    #since a new node is added, check not_inserted whether their parent is the newly added one
    temp_not_inserted=Array.new(not_inserted)
    temp_not_inserted.each{|line|
      reply_to=messages_array[line][8]
      puts "cheking if reply_to ("+reply_to.to_s()+") == "+message[4].to_s()
      if(reply_to==message[4])
        puts "newly added node "+ reply_to.to_s() +" is the parent of not_inserted node "+ messages_array[line][4].to_s()+ ". Adding..."
        branch3=branch2.add_element("branch")
        branch3.add_element("attribute","name"=>"name","value"=>messages_array[line][5])
        branch3.add_element("attribute","name"=>"message_id","value"=>messages_array[line][4])
        branch3.add_element("attribute","name"=>"message_body","value"=>messages_array[line][11])
        not_inserted.delete(line);
      end
    }
   
  else
    is_parent_in_tree=false
    #if no parent found, add into not_inserted
    branch=doc.root.elements["//attribute[@value="+reply_to+"]"]
    
    if(branch==nil)
   # if(!added.include?(reply_to))
      #parent not found
      puts "parent (" + reply_to.to_s() + ") not found.  Line "+ countline.to_s() +" added to not_inserted list." 
      if(!not_inserted.include?(countline))
        not_inserted.push(countline)
        not_inserted_this_time=true
      end
    else
      puts "parent found for " + countline.to_s() +".  Adding to the tree..."
   #   added.push(reply_to)
    #  added.push(topic_id)
      branch=doc.root.elements["//attribute[@value="+reply_to+"]"]
      branch2=branch.add_element("branch")
      branch2.add_element("attribute","name"=>"name","value"=>message[5])
      branch2.add_element("attribute","name"=>"message_id","value"=>message[4])
      
      branch2.add_element("attribute","name"=>"message_body","value"=>message[11])
      
      #since a new node is added to the tree, check whether this is the parent of not_inserted
      #bug probably coming from using the same array for iterating and deleting.  Use different array
      temp_not_inserted=Array.new(not_inserted)
      temp_not_inserted.each{|line|
        reply_to=messages_array[line][8]
        puts "cheking if reply_to ("+reply_to.to_s()+") == "+message[4].to_s()
        if(reply_to==message[4])
          puts "newly added node "+ reply_to.to_s()+" is the parent of not_inserted node "+ messages_array[line][4].to_s()+ ". Adding..."
          branch3=branch2.add_element("branch")
          branch3.add_element("attribute","name"=>"name","value"=>messages_array[line][5])
          branch3.add_element("attribute","name"=>"message_id","value"=>messages_array[line][4])
          branch3.add_element("attribute","name"=>"message_body","value"=>messages_array[line][11])
          not_inserted.delete(line)
        end
      }
    end
    
    num_to_check=not_inserted.length
    puts "not_inserted len: " + not_inserted.length.to_s()
    checked_count=0
    
  end
  countline+=1
}


checkcount=5
while(not_inserted.length>0 and checkcount>0)
  puts "check for the remaining nodes...count " + checkcount.to_s()
  temp_not_inserted=Array.new(not_inserted)
  temp_not_inserted.each{|line|
    reply_to=messages_array[line][8]
    puts "cheking for reply_to ("+reply_to.to_s()+")"
    branch=doc.root.elements["//attribute[@value="+reply_to+"]"]
    
    if(branch!=nil)
      puts "newly added node "+ reply_to.to_s()+" is the parent of not_inserted node "+ messages_array[line][4].to_s()+ ". Adding..."
        branch2=branch.add_element("branch")
        branch2.add_element("attribute","name"=>"name","value"=>messages_array[line][5])
        branch2.add_element("attribute","name"=>"message_id","value"=>messages_array[line][4])
        branch2.add_element("attribute","name"=>"message_body","value"=>messages_array[line][11])
        not_inserted.delete(line)
      end
  }
  checkcount=-checkcount
end
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

