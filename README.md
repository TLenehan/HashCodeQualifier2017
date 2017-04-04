# HashCodeQualifier2017
Slightly simplified (but working) version of our hashcode soluction rewritten to be far easier to read.  
  
## Problem
https://hashcode.withgoogle.com/2017/tasks/hashcode2017_qualification_task.pdf  
  
## Algorithm  
The general idea behind our solution was to iterate through each cache and check which videos saved the most time if added to that cache based on the number of requests for the video made by endpoints connected to the cache and the current latency that video faced reaching those endpoints. An obvious drawback to this design was that we only iterated through the caches once, starting from the 0th to the last. This could create a case where the perfect solution includes adding video X to cahce Y, but X was already added to cachce Y-1 and now the algorithm doesn't see the same benefit of adding video X to cache Y
  
## Difference from original
The biggest difference between this code and what we wrote during the event was I took out a function that was attempting to determine if adding two smaller videos was more efficient than one larger video by testing multiple configurations of videos in the cache as opposed to just adding what the next largest amount of time saved is. While testing multiple configurations would get a better result, we could not make it functional in time for the deadline.
  
## Output format
The output in this is not in the correct format and only prints to the console as it was easier for me to test that way.  
Output format  
Cache number: Total space in cache  
  
Videos added to the cache:  
Video number  current space used in cahce   time saved adding video to cache  
  
Total space used in cache  

## Improvements  
After having to rewrite this code to make it work, and legible it some of the possible improvements became painfully obvious to me, aside from improvements to the algorithm to get a better score, we definitely could have save some running time. Instead checking for every video and every endpoint for each cache, it would have been a lot more beneficial to store which endpoints were connected to a given cahce and by extension which videos are requested by endpoints connected to a given cache. This would have saved us iterating through videos and endpoints that were not even relevant to the the cache we were looking at at the time.
