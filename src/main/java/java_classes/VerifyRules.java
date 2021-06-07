package java_classes;


public class VerifyRules {

    public static void main(String[] args) {
    }
    public static void beRule (String rule){

        // if the word if is present
        String[] split = rule.split(" ");

        int ifpos = -1;
        int andpos = -1;
        int fragCount=0;
        int frag1low = 0;
        int frag1high = split.length-1;
        int frag2low = -1;
        int frag2high = split.length-1;
        int frag3low=-1;
        int frag3high = split.length-1;
        String toPrint ="";

        for(int i=0;i<split.length;i++) {
            if(split[i].equals("if")) {
                ifpos=i;
            }
            if(split[i].equals("and")) {
                andpos=i;
            }
        }

        if(ifpos>-1 && andpos>-1) {
            fragCount=3;
            if (andpos<ifpos) {
                frag1high = andpos;
                frag2low = andpos;
                frag2high = ifpos;
                frag3low = ifpos;
            }else {
                frag1high = ifpos;
                frag2low = ifpos;
                frag2high = andpos;
                frag3low = andpos;
            }

        }else if(ifpos>-1 && andpos==-1) {
            fragCount = 2;
            frag1high = ifpos;
            frag2low = ifpos;
            frag2high = split.length-1;

        }else if(andpos>-1 && ifpos==-1) {
            fragCount = 2;
            frag1high = andpos;
            frag2low = andpos;
            frag2high = split.length-1;

        }else {
            fragCount = 1;
            frag1high=split.length-1;
        }

        //if the word and is present
        if(fragCount ==1) {
            toPrint += (frag1low + " " + frag1high);
        }else if(fragCount ==2) {
            toPrint += (frag1low + " " + frag1high + " ");
            toPrint += (frag2low + " " + frag2high + " ");

        }else if(fragCount ==3) {
            toPrint += (frag1low + " " + frag1high + " ");
            toPrint += (frag2low + " " + frag2high + " ");
            toPrint += (frag3low + " " + frag3high + " ");
        }

        String goPrint = brTest(fragCount,frag1high,toPrint,split,frag2high,frag3high);
        System.out.println(goPrint);

    }

    public static String brTest(int fragCount, int frag1high, String toPrint, String[] split,int frag2high,int frag3high){
        //flags for sentence fragments triggered by keyword booleans
        boolean fragRaiseAlert = false;
        boolean fragHaltInject = false;
        boolean fragUnfit = false;
        boolean fragUnready = false;
        boolean fragCommands = false;
        boolean fragCorrectRate = false;
        boolean fragCorrectDose = false;
        boolean fragComm = false;
        boolean fragGive = false;

        //Arrays of similar words which trigger keyword booleans below
        String[] raiseWords = {"raise","sound", "throw"};
        String[] alertWords = {"alert","alarm"};
        String[] haltWords = {"halt","stop"};
        String[] injectWords = {"inject","injection","injecting","dosing","dose"};
        String[] patientWords = {"patient","person", "recipient", "he", "she"};
        String[] unfitWords = {"unfit", "incapacitated"};
        String[] fitWords = {};
        String[] pcaWords = {"pca","device"};
        String[] unreadyWords = {"unready", "busy"};
        String[] readyWords = {};
        String[] acceptWords = {"accept"};
        String[] authorisedWords = {"authorised", "authorized"};
        String[] commandWords = {"command", "commands"};

        //BR 6
        String[] inject1Words = injectWords;
        String[] correctWords = {"correct", "right", "appropriate"};
        String[] rateWords = {"rate"};

        //BR 7
        String[] inject2Words = injectWords;
        String[] correct1Words = correctWords;
        String[] doseWords = {"dosage", "dose", "volume", "amount"};

        //BR8
        String[] commWords = {"communicate", "interact", "message", "tell"};
        String[] authorised1Words = authorisedWords;

        //BR9
        String[] giveWords = {"give", "administer", "administering", "giving"};
        String[] medWords = {"medicine", "analgesia", "dose"};
        String[] askWords = {"asked", "requested", "asks", "requests", "request"};


        //keyword booleans which trigger sentence flags
        boolean raise=false;
        boolean alert = false;
        boolean halt = false;
        boolean inject = false;
        boolean patient = false;
        boolean unfit = false;
        boolean pca = false;
        boolean unready = false;
        boolean accept = false;
        boolean authorised = false;
        boolean command = false;
        //br6
        boolean inject1 = false;
        boolean correct = false;
        boolean rate = false;
        //br7
        boolean inject2 = false;
        boolean correct1 = false;
        boolean dose = false;
        //br8
        boolean comm = false;
        boolean authorised1 = false;
        //br9
        boolean give = false;
        boolean med = false;
        boolean ask = false;

        String[][] allWords = {raiseWords,alertWords,haltWords,injectWords,patientWords,unfitWords,pcaWords,unreadyWords,acceptWords,authorisedWords,commandWords,inject1Words, correctWords, rateWords, inject2Words, correct1Words, doseWords, commWords, authorised1Words, giveWords, medWords, askWords};
        boolean[] allKeywords = {raise,alert,halt,inject,patient,unfit,pca,unready,accept,authorised,command,inject1, correct, rate, inject2, correct1, dose, comm, authorised1, give, med, ask};
        boolean[] fragAlerts = {fragRaiseAlert, fragHaltInject, fragUnfit, fragUnready, fragCommands, fragCorrectRate, fragCorrectDose, fragComm, fragGive};

        //Searches through sentences that have been split into 3 fragments
        if(fragCount ==3) {

            fragAlerts = threeFragSearch(frag1high, frag2high, frag3high, split, allWords, allKeywords, fragAlerts);

            fragRaiseAlert = fragAlerts[0];
            fragHaltInject = fragAlerts[1];
            fragUnfit = fragAlerts[2];
            fragUnready = fragAlerts[3];

        }


        //Search through sentences that have been split into 2 segments
        if(fragCount ==2) {

            fragAlerts = twoFragSearch(frag1high, frag2high, split, allWords, allKeywords, fragAlerts);

            fragRaiseAlert = fragAlerts[0];
            fragHaltInject = fragAlerts[1];
            fragUnfit = fragAlerts[2];
            fragUnready = fragAlerts[3];

        }

        if(fragCount ==1) {

            fragAlerts = oneFragSearch(frag1high, split, allWords, allKeywords, fragAlerts);

            fragRaiseAlert = fragAlerts[0];
            fragHaltInject = fragAlerts[1];
            fragUnfit = fragAlerts[2];
            fragUnready = fragAlerts[3];
            fragCommands = fragAlerts[4];
            fragCorrectRate = fragAlerts[5];
            fragCorrectDose = fragAlerts[6];
            fragComm = fragAlerts[7];
            fragGive = fragAlerts[8];

        }

        //Which fragment flags match which behaviour rules if any
        if(fragRaiseAlert && fragUnfit) {
            toPrint+="1,";
        }
        if(fragHaltInject && fragUnfit) {
            toPrint+="2,";
        }
        if(fragRaiseAlert && fragUnready) {
            toPrint+="3,";
        }
        if(fragHaltInject && fragUnready) {
            toPrint+="4,";
        }
        if(fragCommands) {
            toPrint+="5,";
        }
        if(fragCorrectRate) {
            toPrint+="6,";
        }
        if(fragCorrectDose) {
            toPrint+="7,";
        }
        if(fragComm) {
            toPrint+="8,";
        }
        if(fragGive) {
            toPrint+="9,";
        }



        toPrint+= " ";
        toPrint += fragCount;
        return toPrint;
    }

    public static boolean[] twoWordCheck(String split[], int i, String[] aWords, boolean a, String[] bWords, boolean b, int upper, boolean flag){
        //Searches for "raise" words and then "alert" words to raise the "raisealert" flag
        //Runs through similar appropriate words to compare against the words in the fragment
        for(int j = 0;j<aWords.length;j++) {
            if (split[i].toLowerCase().equals(aWords[j]))a=true;
        }
        //Runs through similar appropriate words to compare against the words in the fragment
        for(int j = 0;j<bWords.length;j++) {
            if (split[i].toLowerCase().equals(bWords[j]))b=true;
        }

        //If the appropriate words were found then the fragment flag is raised otherwise the word booleans wre ensured to be false
        if((i== (upper))) {
            if ( a==true && b ==true) {
                flag = true;
            }else {
                a=false;
                b=false;
            }
        }

        boolean[] boolArray = {flag,a,b};
        return boolArray;
    }

    public static boolean[] threeWordCheck(String split[], int i, String[] aWords, boolean a, String[] bWords, boolean b, String[] cWords, boolean c, int upper, boolean flag){

        for(int j = 0;j<aWords.length;j++) {
            if (split[i].toLowerCase().equals(aWords[j]))a=true;
        }

        for(int j = 0;j<bWords.length;j++) {
            if (split[i].toLowerCase().equals(bWords[j]))b=true;
        }

        for(int j = 0;j<cWords.length;j++) {
            if (split[i].toLowerCase().equals(cWords[j]))c=true;
        }

        //If the appropriate words were found then the fragment flag is raised otherwise the word booleans wre ensured to be false
        if((i== (upper))) {
            if ( a && b && c) {
                flag = true;
            }else {
                a=false;
                b=false;
                c=false;
            }
        }

        boolean[] boolArray = {flag,a,b,c};
        return boolArray;
    }

    public static boolean[] threeFragSearch(int frag1high, int frag2high, int frag3high, String[] split, String[][] allWords, boolean[] allKeywords, boolean[] fragAlerts) {

        //String[][] allWords = {raiseWords,alertWords,haltWords,injectWords,patientWords,unfitWords,pcaWords,unreadyWords};
        //boolean[] allKeywords = {raise,alert,halt,inject,patient,unfit,pca,unready};
        //boolean[] fragAlerts = {fragRaiseAlert, fragHaltInject, fragUnfit, fragUnready};

        int[] uppers = {frag1high,frag2high,frag3high+1};
        int[]lowers = {0,frag1high+1,frag2high+1};

        for (int x = 0; x<3; x++){
            //Searches the first fragment of the sentence for keywords
            for(int i=lowers[x];i<uppers[x];i++) {

                //Search for "raise" and "alert" words
                boolean[] get=twoWordCheck(split, i, allWords[0], allKeywords[0], allWords[1], allKeywords[1], uppers[x]-1, fragAlerts[0]);
                fragAlerts[0] = get[0];
                allKeywords[0] = get[1];
                allKeywords[1] = get[2];

                //Search for "halt" and "inject" words
                get=twoWordCheck(split, i, allWords[2], allKeywords[2], allWords[3], allKeywords[3], uppers[x]-1, fragAlerts[1]);
                fragAlerts[1] = get[0];
                allKeywords[2] = get[1];
                allKeywords[3] = get[2];

                //Search for "patient" and "unfit" words
                get=twoWordCheck(split, i, allWords[4], allKeywords[4], allWords[5], allKeywords[5], uppers[x]-1, fragAlerts[2]);
                fragAlerts[2] = get[0];
                allKeywords[4] = get[1];
                allKeywords[5] = get[2];

                //Search for "PCA" and "unready" words
                get=twoWordCheck(split, i, allWords[6], allKeywords[6], allWords[7], allKeywords[7], uppers[x]-1, fragAlerts[3]);
                fragAlerts[3] = get[0];
                allKeywords[6] = get[1];
                allKeywords[7] = get[2];

            }

        }
        return fragAlerts;







    }

    public static boolean[] twoFragSearch(int frag1high, int frag2high, String[] split, String[][] allWords, boolean[] allKeywords, boolean[] fragAlerts) {

        //String[][] allWords = {raiseWords,alertWords,haltWords,injectWords,patientWords,unfitWords,pcaWords,unreadyWords};
        //boolean[] allKeywords = {raise,alert,halt,inject,patient,unfit,pca,unready};
        //boolean[] fragAlerts = {fragRaiseAlert, fragHaltInject, fragUnfit, fragUnready};

        int[] uppers = {frag1high,frag2high+1};
        int[]lowers = {0,frag1high+1};

        for (int x = 0; x<2; x++){
            //Searches the first fragment of the sentence for keywords
            for(int i=lowers[x];i<uppers[x];i++) {

                //Search for "raise" and "alert" words
                boolean[] get=twoWordCheck(split, i, allWords[0], allKeywords[0], allWords[1], allKeywords[1], uppers[x]-1, fragAlerts[0]);
                fragAlerts[0] = get[0];
                allKeywords[0] = get[1];
                allKeywords[1] = get[2];

                //Search for "halt" and "inject" words
                get=twoWordCheck(split, i, allWords[2], allKeywords[2], allWords[3], allKeywords[3], uppers[x]-1, fragAlerts[1]);
                fragAlerts[1] = get[0];
                allKeywords[2] = get[1];
                allKeywords[3] = get[2];

                //Search for "patient" and "unfit" words
                get=twoWordCheck(split, i, allWords[4], allKeywords[4], allWords[5], allKeywords[5], uppers[x]-1, fragAlerts[2]);
                fragAlerts[2] = get[0];
                allKeywords[4] = get[1];
                allKeywords[5] = get[2];

                //Search for "PCA" and "unready" words
                get=twoWordCheck(split, i, allWords[6], allKeywords[6], allWords[7], allKeywords[7], uppers[x]-1, fragAlerts[3]);
                fragAlerts[3] = get[0];
                allKeywords[6] = get[1];
                allKeywords[7] = get[2];

            }

        }
        return fragAlerts;







    }

    public static boolean[] oneFragSearch(int frag1high, String[] split, String[][] allWords, boolean[] allKeywords, boolean[] fragAlerts) {

        //String[][] allWords = {raiseWords,alertWords,haltWords,injectWords,patientWords,unfitWords,pcaWords,unreadyWords};
        //boolean[] allKeywords = {raise,alert,halt,inject,patient,unfit,pca,unready};
        //boolean[] fragAlerts = {fragRaiseAlert, fragHaltInject, fragUnfit, fragUnready};

        int[] uppers = {frag1high+1};
        int[]lowers = {0};

        for (int x = 0; x<1; x++){
            //Searches the the sentence for keywords
            for(int i=lowers[x];i<uppers[x];i++) {

                //Search for "raise" and "alert" words
                boolean[] get=twoWordCheck(split, i, allWords[0], allKeywords[0], allWords[1], allKeywords[1], uppers[x]-1, fragAlerts[0]);
                fragAlerts[0] = get[0];
                allKeywords[0] = get[1];
                allKeywords[1] = get[2];

                //Search for "halt" and "inject" words
                get=twoWordCheck(split, i, allWords[2], allKeywords[2], allWords[3], allKeywords[3], uppers[x]-1, fragAlerts[1]);
                fragAlerts[1] = get[0];
                allKeywords[2] = get[1];
                allKeywords[3] = get[2];

                //Search for "patient" and "unfit" words
                get=twoWordCheck(split, i, allWords[4], allKeywords[4], allWords[5], allKeywords[5], uppers[x]-1, fragAlerts[2]);
                fragAlerts[2] = get[0];
                allKeywords[4] = get[1];
                allKeywords[5] = get[2];

                //Search for "PCA" and "unready" words
                get=twoWordCheck(split, i, allWords[6], allKeywords[6], allWords[7], allKeywords[7], uppers[x]-1, fragAlerts[3]);
                fragAlerts[3] = get[0];
                allKeywords[6] = get[1];
                allKeywords[7] = get[2];

                //Search for "comm" and "authorised" words
                get=twoWordCheck(split, i, allWords[17], allKeywords[17], allWords[18], allKeywords[18], uppers[x]-1, fragAlerts[7]);
                fragAlerts[7] = get[0];
                allKeywords[17] = get[1];
                allKeywords[18] = get[2];

                //Three word checks
                //Search for "accept" and "authorised" and "command" words
                get=threeWordCheck(split, i, allWords[8], allKeywords[8], allWords[9], allKeywords[9], allWords[10], allKeywords[10], uppers[x]-1, fragAlerts[4]);
                fragAlerts[4] = get[0];
                allKeywords[8] = get[1];
                allKeywords[9] = get[2];
                allKeywords[10] = get[3];

                //Search for "inject" "correct" "rate" words
                get=threeWordCheck(split, i, allWords[11], allKeywords[11], allWords[12], allKeywords[12], allWords[13], allKeywords[13], uppers[x]-1, fragAlerts[5]);
                fragAlerts[5] = get[0];
                allKeywords[11] = get[1];
                allKeywords[12] = get[2];
                allKeywords[13] = get[3];

                //Search for "inject" "correct" "dose" words
                get=threeWordCheck(split, i, allWords[14], allKeywords[14], allWords[15], allKeywords[15], allWords[16], allKeywords[16], uppers[x]-1, fragAlerts[6]);
                fragAlerts[6] = get[0];
                allKeywords[14] = get[1];
                allKeywords[15] = get[2];
                allKeywords[16] = get[3];

                //Search for "inject" "correct" "dose" words
                get=threeWordCheck(split, i, allWords[19], allKeywords[19], allWords[20], allKeywords[20], allWords[21], allKeywords[21], uppers[x]-1, fragAlerts[8]);
                fragAlerts[8] = get[0];
                allKeywords[19] = get[1];
                allKeywords[20] = get[2];
                allKeywords[21] = get[3];

            }

        }
        return fragAlerts;







    }

}

