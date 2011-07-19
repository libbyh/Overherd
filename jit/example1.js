var labelType, useGradients, nativeTextSupport, animate;

(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport 
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
  nativeTextSupport = labelType == 'Native';
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
  elem: false,
  write: function(text){
    if (!this.elem) 
      this.elem = document.getElementById('log');
    this.elem.innerHTML = text;
    this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
  }
};



// Children is list.
// { "children": [{self}, {self}], "data": {}, "id": "the_id", "name": "the_name" }

function init(){
  //init data
  var json ={
   "children": [
      {
         "children": [
            {
               "children": [], 
               "data": {
                  "content": "\n", 
                  "date_time": "2011-07-06 08:40:00", 
                  "$area": 1.0, 
                  "$color": "#5C5858", 
                  "author": "ferret"
               }, 
               "id": "6657584", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [
                        {
                           "children": [], 
                           "data": {
                              "content": "\n", 
                              "date_time": "2011-07-09 18:44:00", 
                              "$area": 1.0, 
                              "$color": "#5C5858", 
                              "author": "SBakall"
                           }, 
                           "id": "6666411", 
                           "name": "Reply"
                        }
                     ], 
                     "data": {
                        "content": "\n One of the Lettuce restaurants- Hub 51 would probably be a good fit for your group if they can accomodate you. It's got a wide dinner menu- from sushi to chicken nachos, but the food is good and the place is very lively and always packed. It's also open for brunch on the weekends, although I don't think their brunch menu is that creative. \n", 
                        "date_time": "2011-07-06 10:25:00", 
                        "$area": 5.0, 
                        "$color": "#5C5858", 
                        "author": "ms. mika"
                     }, 
                     "id": "6657899", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n With that size, you will want a place that can give you a private room.  Give the private party folks at Lettuce Entertain You a call.  Lettuce Entertain You is a group of around 40-50 restaurants, almost all in the Chicago area and most in the city.  They vary in concept, and include everything from fine dining (Everest, TRU, L2O) to cheap eats and everything in between.  Many of them are the best of their kind in the city.  They have one group that can coordinate reservations for any of their restaurants.  Their private party coordinators even have their own website, at ", 
                  "date_time": "2011-07-06 08:50:00", 
                  "$area": 6.0, 
                  "$color": "#5C5858", 
                  "author": "nsxtasy"
               }, 
               "id": "6657607", 
               "name": "Reply"
            }
         ], 
         "data": {
            "content": "\n Hi all,  I'm a New Yorker who decided to celebrate my birthday in Chicago with a Cubs game and some good eats.  There will be about 20-25 of us in total for the weekend which I know is a big group, and we're only 3 weeks away!  Does anyone have any suggestions of where to go that can accommodate us?  Both dinner and brunch recommendations would be greatly appreciated.  We're all late 20s/early 30s and staying downtown (River North/Gold Coast) and would like to stay within walking distance.  Open to all types of a food.  There will be foodies and non-foodie's.  I'd love to keep each meal under $50 if possible per person.    Appreciate any help! \n", 
            "date_time": "2011-07-06 07:35:00", 
            "$area": 6.0, 
            "$color": "#151B54", 
            "author": "AWK724"
         }, 
         "id": "6657394", 
         "name": "BIG Group Dinner for New to Chicago"
      }, 
      {
         "children": [
            {
               "children": [
                  {
                     "children": [
                        {
                           "children": [], 
                           "data": {
                              "content": "\n Jin Ju is not a Korean restaurant, in my opinion.  It's like saying that Chipotle is authentic Mexican American food. \n", 
                              "date_time": "2009-10-24 08:21:00", 
                              "$area": 3.0, 
                              "$color": "#5C5858", 
                              "author": "jituji70"
                           }, 
                           "id": "5127334", 
                           "name": "Reply"
                        }
                     ], 
                     "data": {
                        "content": "\n Jin Ju is delicious (I would call it a little bit \"chi chi\", maybe more because I'm indignant that the Caucasian waiter corrected my pronunciation - I'm Korean) but you can find the same bold flavors for much less at nearly any restaurant in K-town.  If you want to pay extra for lovely dishes, fancy drinks, and trendy atmosphere then Jin Ju is for you.  Otherwise, look along Lincoln and Lawrence. \n", 
                        "date_time": "2008-01-21 19:43:00", 
                        "$area": 5.0, 
                        "$color": "#5C5858", 
                        "author": "heartofthevalley"
                     }, 
                     "id": "3318920", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n Jin Ju in Andersonville is fantastic - one of my favorite restaurants, in fact. I don't think I'd call it \"chi chi\" but it's not a hole in the wall. It's a nice restaurant, entrees are in the low teens. The soju martinis are really good also. I love the Chop Chae as well as the Bi Bim Bap. The address is 5203 N. Clark St. Metromix has a review at ", 
                  "date_time": "2008-01-18 11:13:00", 
                  "$area": 5.0, 
                  "$color": "#5C5858", 
                  "author": "KevinAllTerrain"
               }, 
               "id": "3308864", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [], 
                     "data": {
                        "content": "\n Solga is great.  It's my parents' go-to place when we have guests and want to take them for an authentic Korean meal that is a little fancier than the places we usually go.   For a true hole in the wall, my favorite is the kalbi house on Kimball and Lawrence.  I can't remember the name for the life of me but we've been going there since I was a baby.  It's next to a day care center. \n", 
                        "date_time": "2008-01-19 17:04:00", 
                        "$area": 5.0, 
                        "$color": "#5C5858", 
                        "author": "Kwonton"
                     }, 
                     "id": "3312741", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n My wife and I areKorean, and these are our favorite places to go:  In the city area: \nSolga, on Lincoln between Peterson and California. Good variety of traditional Korean dishes, barbeque, and soups. Clean restaurant, usually pretty busy but has a parking lot which is key. Pretty good dol sot bi bim bap. Good naeng myun (cold noodle soup) as well.\nIn the Northwest Suburbs:\nCho Dang Tofu, on Algonquin Rd near Dempster in Mount Prospect (they have a website for directions, and another restaurant in Naperville). They basically specialize primarily in a very spicy tofu soup called soon du bu which is served boiling hot. Very very good. Clean, simple restaurant, but small. Good bi bim bap.\nJigol Jigol (not sure of the English spelling), southeast corner of Roselle and Golf Rd in Schaumburg. Specializes in BBQ right at the table. We like it for the self-serve table for side vegetables that you grill with your meat. They have an all-you-can-eat special that is good if you have 4 people or more. New place, kind of chi-chi but definitely authentic. Feels more like Korean restaurants in the NYC/NJ area.\nKoreana - Salem and Golf Rd in Schaumburg - a little divey but they've renovated recently, our favorite dol sot bi bim bap. They also have good large casserole dishes served on table top pots for 2 or more.  Have been to Jin Ju -- it's not bad, but it is kind of Americanized Korean food in how the food is prepared and served, to be honest. \n", 
                  "date_time": "2008-01-18 19:26:00", 
                  "$area": 9.0, 
                  "$color": "#5C5858", 
                  "author": "Dporter"
               }, 
               "id": "3310588", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [], 
                     "data": {
                        "content": "\n Cho Sun Ok is excellent, and very authentic.  So authentic that we can't go without our Korean-speaking friend.  It's his favorite Korean restaurant in the city. \n", 
                        "date_time": "2009-10-24 14:56:00", 
                        "$area": 3.0, 
                        "$color": "#5C5858", 
                        "author": "thelegalfoodie"
                     }, 
                     "id": "5128069", 
                     "name": "Reply"
                  }, 
                  {
                     "children": [], 
                     "data": {
                        "content": "\n So sad that ", 
                        "date_time": "2011-03-18 07:21:00", 
                        "$area": 1.0, 
                        "$color": "#5C5858", 
                        "author": "geg5150"
                     }, 
                     "id": "6392447", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n My personal favorite is Kang Nam\n4849 N. Kedzie Ave. \nChicago, IL 60625\n773-539-2524\nVery casual place, excellent food. Better in my opinion than San Soo Gap San. If you want BYO, Cho Sun Ok on Lincoln north of Irving is a good option. \n", 
                  "date_time": "2008-01-24 11:02:00", 
                  "$area": 4.0, 
                  "$color": "#5C5858", 
                  "author": "jayh"
               }, 
               "id": "3328316", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [], 
                     "data": {
                        "content": "\n Don't know if you're looking for a place in the city, but Hanabi in Lincoln Park has a serviceable bibimbap. \n", 
                        "date_time": "2008-01-26 17:00:00", 
                        "$area": 3.0, 
                        "$color": "#5C5858", 
                        "author": "jlklp"
                     }, 
                     "id": "3336253", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n I completely agree with the recommendations for Solga and JinJu. Jin Ju is less traditional but still excellent soon tofu. Solga has the best naeng myun and an awesome haemul pajun. \n", 
                  "date_time": "2008-01-25 20:07:00", 
                  "$area": 4.0, 
                  "$color": "#5C5858", 
                  "author": "lvhkitty"
               }, 
               "id": "3334175", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [
                        {
                           "children": [
                              {
                                 "children": [], 
                                 "data": {
                                    "content": "\n It's still the best Korean place in town \n", 
                                    "date_time": "2011-05-05 20:57:00", 
                                    "$area": 2.0, 
                                    "$color": "#5C5858", 
                                    "author": "ALITIN"
                                 }, 
                                 "id": "6518443", 
                                 "name": "Reply"
                              }
                           ], 
                           "data": {
                              "content": "\n We called the other night.  They no longer offer private cooking lessons.  : - ( \n", 
                              "date_time": "2011-03-16 18:17:00", 
                              "$area": 3.0, 
                              "$color": "#5C5858", 
                              "author": "lgss"
                           }, 
                           "id": "6388750", 
                           "name": "Reply"
                        }
                     ], 
                     "data": {
                        "content": "\n Our favorite restaurant of all in Chicago is ", 
                        "date_time": "2011-03-13 15:45:00", 
                        "$area": 2.0, 
                        "$color": "#5C5858", 
                        "author": "lgss"
                     }, 
                     "id": "6379629", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n My favorite Korean spot is ", 
                  "date_time": "2009-10-26 10:23:00", 
                  "$area": 2.0, 
                  "$color": "#5C5858", 
                  "author": "ALITIN"
               }, 
               "id": "5131836", 
               "name": "Reply"
            }, 
            {
               "children": [], 
               "data": {
                  "content": "\n Joongboo Market on Kimball south of 90/94. Eat in dining area inside a market cafeteria style. Good prices, great food. Sounds like what your looking for. \n", 
                  "date_time": "2011-03-13 21:05:00", 
                  "$area": 3.0, 
                  "$color": "#5C5858", 
                  "author": "alnery"
               }, 
               "id": "6380333", 
               "name": "Reply"
            }, 
            {
               "children": [], 
               "data": {
                  "content": "\n A good Chinese-Korean place out in the 'burbs is Ming's. It's a restaurant inside the Schaumburg Radisson on Algonquin Rd just west of Route 53.   Not sure they do a good job of advertising themselves, but the Korean menu there is very good, especially if you're into the fried stuff - gambonkgee is really good and crispy there, and their ja jang myun and jamboong are both quite good too. Even the ool-myun is good. One non-Korean dish we love there is the salt and pepper fried squid -- you see that dish more on the east coast than here in the midwest. All the wait staff are Korean, even though it's technically a Chinese restaurant (they have separate Chinese and Korean menus).  The Chinese fare is pretty standard stuff.  Great place to take kids because it's big, very clean (because it's maintained by the Radisson; used to be a steakhouse restaurant) and spacious -- and seemingly never busy, at least on the weekends (we hear they keep busy during the week with Korean Air Lines flight attendants and pilots who are on layovers). \n", 
                  "date_time": "2011-04-30 09:59:00", 
                  "$area": 8.0, 
                  "$color": "#5C5858", 
                  "author": "Dporter"
               }, 
               "id": "6505797", 
               "name": "Reply"
            }, 
            {
               "children": [], 
               "data": {
                  "content": "\n We have Korean food every week and hands down - THE BEST dolsot bi bim bop (served sizzling hot in a stone bowl) in the city is at Ban Po Jung located at 3450 W Foster Ave, on the East end of Northeastern University.  It's owned by the cutest couple who work there every day..   The place is filled with Korean diners (always a good thing when in a Korean restaurant!) and the prices are very reasonable.   Along with their many side dishes, they also serve up a free veggie pancake and a fried fish for each person.  Don't let the looks of the fish fool you because it is absolutely delish!  You ask why I say THE BEST bi bim bop?  Once you finish the bibimbop, underneath, is the most delicious crispy rice \"crust\" that just slips out easily from the bowl for that final delicious blow..  In fact, there have been times when I couldn't finish the bibimbop and had to scoop it out onto my plate just so I could attack the crispy rice crust below.  Not sure what they do to their bowl, but this is the only Korean restaurant I've ever eaten this dish at where you don't have to jackhammer the crust out.    Enjoy! \n", 
                  "date_time": "2011-07-09 18:25:00", 
                  "$area": 8.0, 
                  "$color": "#5C5858", 
                  "author": "SBakall"
               }, 
               "id": "6666382", 
               "name": "Reply"
            }
         ], 
         "data": {
            "content": "\n Anyone have a favorite Korean spot to suggest? It needn't be any chi-chi. In fact, the more mom-and-pop, authentic little store front the better. I just want some good bi bim bop.  Thanks. \n", 
            "date_time": "2008-01-17 18:22:00", 
            "$area": 4.0, 
            "$color": "#151B54", 
            "author": "las375"
         }, 
         "id": "3306686", 
         "name": "Best Korean in Chicago"
      }, 
      {
         "children": [
            {
               "children": [
                  {
                     "children": [], 
                     "data": {
                        "content": "\n Yes, they opened this past week according to their FB page. \n", 
                        "date_time": "2011-07-09 12:31:00", 
                        "$area": 2.0, 
                        "$color": "#5C5858", 
                        "author": "altobarb"
                     }, 
                     "id": "6665784", 
                     "name": "Reply"
                  }, 
                  {
                     "children": [], 
                     "data": {
                        "content": "\n", 
                        "date_time": "2011-07-09 16:03:00", 
                        "$area": 1.0, 
                        "$color": "#5C5858", 
                        "author": "mrsdebdav"
                     }, 
                     "id": "6666158", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n Are they actually open yet? \n", 
                  "date_time": "2011-07-09 12:19:00", 
                  "$area": 2.0, 
                  "$color": "#5C5858", 
                  "author": "nsxtasy"
               }, 
               "id": "6665768", 
               "name": "Reply"
            }
         ], 
         "data": {
            "content": "\n I've been reading about Tribute opening in the Essex.  Menu looks really interesting to me, but am too far away to eat there yet (hoping to remedy that before summer is over).    So I was wondering if anyone's been there and what you think.  Thanks! \n", 
            "date_time": "2011-07-09 12:07:00", 
            "$area": 4.0, 
            "$color": "#151B54", 
            "author": "altobarb"
         }, 
         "id": "6665739", 
         "name": "Tribute"
      }, 
      {
         "children": [
            {
               "children": [], 
               "data": {
                  "content": "\n In downtown Naperville, you will find The ", 
                  "date_time": "2011-07-08 15:32:00", 
                  "$area": 2.0, 
                  "$color": "#5C5858", 
                  "author": "CJT"
               }, 
               "id": "6664176", 
               "name": "Reply"
            }
         ], 
         "data": {
            "content": "\n I am looking for a good Thai restaurant within 30 min. driving distance of Naperville.  Any suggestions?  I would prefer to go somewhere in the suburbs.  We need to be able to seat about 7-10 people at a table. \n", 
            "date_time": "2011-07-08 11:47:00", 
            "$area": 4.0, 
            "$color": "#151B54", 
            "author": "shorty68"
         }, 
         "id": "6663556", 
         "name": "Need Thai restaurant recommendation for western suburbs"
      }, 
      {
         "children": [
            {
               "children": [], 
               "data": {
                  "content": "\n I have been to both and personally prefer Chicago Cut. The menu is more robust, the food is excellent and the service was excellent. The atmosphere is steakhouse, but a little more modern/luxurious to me than DBP. There are great views (and a patio) of the River and the Loop.   I'm not the kind of person that just goes for steak + starch, so I think the menu is a HUGE plus for me...  Perhaps the one time at DBP was a fluke, but I've now been back to Chicago Cut several times and each has been really great. \n", 
                  "date_time": "2011-07-07 13:51:00", 
                  "$area": 6.0, 
                  "$color": "#5C5858", 
                  "author": "italophile"
               }, 
               "id": "6661168", 
               "name": "Reply"
            }, 
            {
               "children": [
                  {
                     "children": [], 
                     "data": {
                        "content": "\n I haven't been as impressed w/ this ", 
                        "date_time": "2011-07-08 15:21:00", 
                        "$area": 2.0, 
                        "$color": "#5C5858", 
                        "author": "italophile"
                     }, 
                     "id": "6664153", 
                     "name": "Reply"
                  }
               ], 
               "data": {
                  "content": "\n Chicago cut or capital grill \n", 
                  "date_time": "2011-07-07 21:38:00", 
                  "$area": 2.0, 
                  "$color": "#5C5858", 
                  "author": "feysul"
               }, 
               "id": "6662211", 
               "name": "Reply"
            }
         ], 
         "data": {
            "content": "\n which has the edge on food, atmo, service? \n", 
            "date_time": "2011-07-07 12:38:00", 
            "$area": 2.0, 
            "$color": "#151B54", 
            "author": "sake"
         }, 
         "id": "6660964", 
         "name": "Choose one - David Burke Primehouse or Chicago Cut"
      }
   ], 
   "data": {}, 
   "id": "http://chowhound.chow.com/boards/7", 
   "name": "Chicago Board"
} ;

  //end
  //init TreeMap
  var tm = new $jit.TM.Squarified({
    //where to inject the visualization
    injectInto: 'infovis',
    //parent box title heights
    titleHeight: 15,
    //enable animations
    animate: animate,
    //box offsets
    offset: 1,
    //Attach left and right click events
    Events: {
      enable: true,
      onClick: function(node) {
        if(node) tm.enter(node);
      },
      onRightClick: function() {
        tm.out();
      }
    },
    duration: 500,
    //Enable tips
    Tips: {
      enable: true,
      //add positioning offsets
      offsetX: 20,
      offsetY: 20,
      //implement the onShow method to
      //add content to the tooltip when a node
      //is hovered
      onShow: function(tip, node, isLeaf, domElement, content) {
        var html = "<div class=\"tip-title\">" + node.name 
          + "</div><div class=\"tip-text\">";
        var data = node.data;
        if(data.author) {
          html += "<p>author: " + data.author + "<\p>";
        }
        if(data.date_time) {
            html += "date/time: " + data.date_time; 
            //html += "<img src=\""+ data.image +"\" class=\"album\" />";
        }
        if(data.reply_count){
            html += "replies: " + reply_count;
        }
        if(data.content) {
            content_html = data.content;
        }
        tip.innerHTML =  html; 
        document.getElementById('bottom-container').innerHTML = content_html;
      }  
      // onShow: function(
    },
    //Add the name of the node in the correponding label
    //This method is called once, on label creation.
    onCreateLabel: function(domElement, node){
        domElement.innerHTML = node.name;
        var style = domElement.style;
        style.display = '';
        style.border = '2px solid transparent';
        domElement.onmouseover = function() {
          style.border = '2px solid red'; //#9FD4FF
        };
        domElement.onmouseout = function() {
          style.border = '2px solid transparent';
        };
    }
  });
  tm.loadJSON(json);
  tm.refresh();
  //end
  //add events to radio buttons
  var sq = $jit.id('r-sq'),
      st = $jit.id('r-st'),
      sd = $jit.id('r-sd');
  var util = $jit.util;
  util.addEvent(sq, 'change', function() {
    if(!sq.checked) return;
    util.extend(tm, new $jit.Layouts.TM.Squarified);
    tm.refresh();
  });
  util.addEvent(st, 'change', function() {
    if(!st.checked) return;
    util.extend(tm, new $jit.Layouts.TM.Strip);
    tm.layout.orientation = "v";
    tm.refresh();
  });
  util.addEvent(sd, 'change', function() {
    if(!sd.checked) return;
    util.extend(tm, new $jit.Layouts.TM.SliceAndDice);
    tm.layout.orientation = "v";
    tm.refresh();
  });
  //add event to the back button
  var back = $jit.id('back');
  $jit.util.addEvent(back, 'click', function() {
    tm.out();
  });
}
