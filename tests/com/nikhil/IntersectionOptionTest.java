package com.nikhil;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionOptionTest {

    @Test
    void intersectsWith() {
        // data setup :

        // words
        Word mars = new Word("Mars","Fourth Planet");
        Word earth = new Word("Earth","Third Planet");
        Word ear = new Word("Ear","Organ for auditory reception");
        Word eyes = new Word("Eyes","Organ for visual perception");
        Word august = new Word("August","Eighth month");
        Word mugger = new Word("Mugger","A person who attacks and robs another in a public place");
        Word one = new Word("One","First number after 0");
        Word tapestry = new Word("Tapestry","Used in reference to an intricate or complex sequence" +
                " of events");
        Word bad = new Word("Bad","Opposite of good");
        Word lies = new Word("Lies","Numerous deceitful facts");
        Word tut = new Word("Tut","Short for tutorial");
        Word me = new Word("Me","Used by speaker to refer to himself or herself");
        Word brer = new Word("Brer","Used as an informal title before a man's name");
        Word grey = new Word("Grey","Of a colour intermediate between black and white, as of ashes" +
                " or lead");
        Word marsh = new Word("Marsh","an area of low-lying land which is flooded in wet seasons or" +
                " at high tide, and typically remains waterlogged at all times");
        Word state = new Word("State","The particular condition that someone or something is in at a " +
                "specific time");
        Word rip = new Word("Rip","Tear or pull (something) quickly or forcibly away from something " +
                "or someone");
        Word palm = new Word("Palm","The inner surface of the hand between the wrist and fingers");
        Word slayer = new Word("Slayer","Someone who kills a person or animal in a violent way");
        Word team = new Word("Team","A group of players forming one side in a competitive game or sport");
        Word great = new Word("Great","Of an extent, amount, or intensity considerably above average");
        Word plate = new Word("Plate","A flat dish, typically circular and made of china, from which " +
                "food is eaten or served");
        Word sagittarius = new Word("Sagittarius","A zodiac sign one of whose symbol is a creature " +
                "that is half horse, half man, shooting an arrow");
        Word romantic = new Word("Romantic","Conducive to or characterized by the expression of love");
        Word harmony = new Word("Harmony","The quality of forming a pleasing and consistent whole");


        // put words in list
        ArrayList<Word> wordList = new ArrayList<>();
        wordList.add(mars);
        wordList.add(earth);
        wordList.add(ear);
        wordList.add(eyes);
        wordList.add(august);
        wordList.add(mugger);
        wordList.add(one);
        wordList.add(tapestry);
        wordList.add(bad);
        wordList.add(lies);
        wordList.add(tut);
        wordList.add(me);
        wordList.add(brer);
        wordList.add(grey);
        wordList.add(marsh);
        wordList.add(state);
        wordList.add(rip);
        wordList.add(palm);
        wordList.add(slayer);
        wordList.add(team);
        wordList.add(great);
        wordList.add(plate);
        wordList.add(sagittarius);
        wordList.add(romantic);
        wordList.add(harmony);

        // place a few words
        // vertical words
        tapestry.placeAt(4,4,true);
        lies.placeAt(6,-1,true);
        mars.placeAt(3,10,true);
        one.placeAt(6,8,true);
        ear.placeAt(11,7,true);
        mugger.placeAt(8,14,true);

        // horizontal words
        harmony.placeAt(-2,-2,false);
        august.placeAt(8,0,false);
        brer.placeAt(10,3,false);
        state.placeAt(6,10,false);
        great.placeAt(10,14,false);

        // find intersections options of placed words
        List<IntersectionOption> tapestryIntersections = tapestry.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> liesIntersections = lies.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> marsIntersections = mars.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> oneIntersections = one.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> earIntersections = ear.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> muggerIntersections = mugger.findAllIntersectionOptions(wordList,false);

        List<IntersectionOption> harmonyIntersections = harmony.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> augustIntersections = august.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> brerIntersections = brer.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> stateIntersections = state.findAllIntersectionOptions(wordList,false);
        List<IntersectionOption> greatIntersections = great.findAllIntersectionOptions(wordList,false);


        // all negative test cases
        IntersectionOption greyAugust = findIntersectionOptionInList(augustIntersections,2,grey,0);
        IntersectionOption greyTapestry = findIntersectionOptionInList(tapestryIntersections,7,grey,3);
        assertNull(greyAugust.intersectsWith(greyTapestry));

        IntersectionOption badBrer = findIntersectionOptionInList(brerIntersections,0,bad,0);
        assertNull(badBrer.intersectsWith(badBrer));

        IntersectionOption meMars = findIntersectionOptionInList(marsIntersections,0,me,0);
        IntersectionOption earthState = findIntersectionOptionInList(stateIntersections,1,earth,3);
        assertNull(meMars.intersectsWith(earthState));

        IntersectionOption ripBrer = findIntersectionOptionInList(brerIntersections,3,rip,0);
        IntersectionOption palmEar = findIntersectionOptionInList(earIntersections,1,palm,1);
        assertNull(ripBrer.intersectsWith(palmEar));

        IntersectionOption teamState = findIntersectionOptionInList(stateIntersections,1,team,0);
        IntersectionOption palmMugger = findIntersectionOptionInList(muggerIntersections,0,palm,3);
        assertNull(teamState.intersectsWith(palmMugger));

        IntersectionOption greyHarmony = findIntersectionOptionInList(harmonyIntersections,2,grey,1);
        IntersectionOption plateTapestry = findIntersectionOptionInList(tapestryIntersections,0,plate,3);
        assertNull(greyHarmony.intersectsWith(plateTapestry));

        IntersectionOption teamMars = findIntersectionOptionInList(marsIntersections,0,team,3);
        IntersectionOption romanticAugust = findIntersectionOptionInList(augustIntersections,0,romantic,3);
        assertNull(teamMars.intersectsWith(romanticAugust));


        // all positive test cases
        IntersectionOption tapestryEar = findIntersectionOptionInList(earIntersections,2,tapestry,6);
        IntersectionOption eyesBrer = findIntersectionOptionInList(brerIntersections,2,eyes,0);
        assertNotNull(tapestryEar.intersectsWith(eyesBrer));

        IntersectionOption palmTapestry = findIntersectionOptionInList(tapestryIntersections,2,palm,0);
        IntersectionOption slayerBrer = findIntersectionOptionInList(brerIntersections,3,slayer,5);
        assertNotNull(palmTapestry.intersectsWith(slayerBrer));

        IntersectionOption earthOne = findIntersectionOptionInList(oneIntersections,2,earth,0);
        IntersectionOption tutState = findIntersectionOptionInList(stateIntersections,1,tut,0);
        assertNotNull(earthOne.intersectsWith(tutState));

        IntersectionOption teamMugger = findIntersectionOptionInList(muggerIntersections,0,team,3);
        assertNotNull(tutState.intersectsWith(teamMugger));

        IntersectionOption tutGreat = findIntersectionOptionInList(greatIntersections,4,tut,0);
        IntersectionOption earthMugger = findIntersectionOptionInList(muggerIntersections,4,earth,0);
        assertNotNull(tutGreat.intersectsWith(earthMugger));

        IntersectionOption teamAugust = findIntersectionOptionInList(augustIntersections,0,team,2);
        IntersectionOption plateLies = findIntersectionOptionInList(liesIntersections,0,plate,1);
        assertNotNull(teamAugust.intersectsWith(plateLies));

        IntersectionOption sagittariusHarmony = findIntersectionOptionInList(harmonyIntersections,1,sagittarius,1);
        IntersectionOption romanticTapestry = findIntersectionOptionInList(tapestryIntersections,0,romantic,4);
        assertNotNull(sagittariusHarmony.intersectsWith(romanticTapestry));

    }

    @Test
    void projectedLocationOfCrossingWord() {

    }

    private IntersectionOption findIntersectionOptionInList(List<IntersectionOption> intersectionOptionList,int sourceIndex, Word crossingWord,int crossingIndex){
        for (IntersectionOption intersectionOption : intersectionOptionList){
            if(intersectionOption.crossing == crossingWord && intersectionOption.crossingIndex == crossingIndex
                    && intersectionOption.sourceIndex == sourceIndex){
                return intersectionOption;
            }
        }
        return null;
    }

}