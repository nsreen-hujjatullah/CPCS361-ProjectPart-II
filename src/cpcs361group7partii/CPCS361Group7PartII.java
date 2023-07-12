/*
---------------------------------------
        CPCS-361- Project - Part II 
---------------------------------------
         GROUP (7) MEMBER - B1
---------------------------------------

---------------------------------------
Instructions for running the programs
---------------------------------------
1. Verify that (addresses.txt & correct.txt) 
   are in the correct file (attached in zip file)

2. Simply run the program to see the results.

3.Now that you're ready, I wish you a successful run :)

---------------------------------------
 */
package cpcs361group7partii;
// imports several packages which are necessary for this project

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author nsree
 */
public class CPCS361Group7PartII {

    /**
     * @param args the command line arguments
     */
    //-----------------------------------------------------------------
    //Initialize several static variables used throughout the code
    static int pageTableSize = 256;
    static int Framesize = 256;
    static int physicalMemory256 = 256;
    static int[] pageTable = new int[pageTableSize];
    static int Laddress;
    static byte value;
    static byte[] physicalMemory = new byte[Framesize * physicalMemory256];
    static int[][] MyTestData = new int[100][2];
    static int[][] MyTestData2 = new int[133][2];
    static int physMemory128 = 128;
    static byte[] physicalMemory128 = new byte[physMemory128 * physicalMemory256];
    static ArrayList<Integer> rand_List128 = new ArrayList<Integer>();
    // Random List to stor numbers from 0 to 132 in random order    
    static ArrayList<Integer> rand_List133 = new ArrayList<Integer>();
    static int offset = 0;
    static int FramNumber = 0;
    static byte values = 0;
    static int logicRandomAddres = 0;
    static int index = 0;
    static ArrayList<Integer> listIndex = new ArrayList<Integer>();
    static ArrayList<Byte> listValue = new ArrayList<Byte>();

    //-----------------------------------------------------------------------
    public static void main(String[] args) throws FileNotFoundException {
        File ADDRESS = new File("addresses.txt");
        Scanner readAddress = new Scanner(ADDRESS); // READ DATA FROM FILE
        // Check the input file if exist.
        if (!ADDRESS.exists()) {
            System.out.println("File," + ADDRESS.getName() + "does not exist");
            System.exit(0);
        }
        File CORRCET = new File("correct.txt");
        Scanner readCorrect = new Scanner(CORRCET); // READ DATA FROM FILE
        // Check the input file if exist.
        if (!CORRCET.exists()) {
            System.out.println("File," + CORRCET.getName() + "does not exist");
            System.exit(0);
        }
        // Initialize pageTable array with -1 values 
        for (int i = 0; i < pageTable.length; i++) {
            pageTable[i] = -1;
        }
        // Initialize physicalMemory array with -1 values
        for (int i = 0; i < physicalMemory.length; i++) {
            physicalMemory[i] = -1;
        }
        // --------------------------------------------------------

        for (int i = 0; i < MyTestData.length; i++) {
            //
            Laddress = readAddress.nextInt();
            value = readCorrect.nextByte();
            //
            for (int j = 0; j < MyTestData[i].length; j++) {
                MyTestData[i][0] = Laddress;
                MyTestData[i][1] = value;
            }
            //-----------------------------------------------------------------
            //To mask the right most 16 bit of the logical address 
            int maskLaddress = (int) (Laddress % Math.pow(2, 16));
            //-------------------------------------------------------------
            int pageNumber = calculatePageNumber(maskLaddress);
            // calculateOffset
            int offset = calculateOffset(maskLaddress);
            int FramNumber = 0;
            for (int j = 0; j < pageTable.length; j++) {
                if (j == pageNumber) {
                    if (pageTable[j] != -1) {
                        FramNumber = pageTable[j];
                        pageTable[j] = FramNumber;
                    } else {
                        FramNumber = generateUniqueRandomNumber(256); // call 
                        pageTable[j] = FramNumber;
                    }
                }
            }
            // Calculate index for each random value and its corresponding frame
            int index = calculateIndex(FramNumber, offset);
            // Copy the values from the listValue ArrayList into the physical memory array
            for (int k = 0; k < physicalMemory.length; k++) {
                physicalMemory[index] = value;
            }

        }
// calling sevral method 
        Test1();
        Test2();
        PageReplacement(readCorrect);
        Test3();
        Test4();
    }// end main method 

    //--------------------------------------------------------------------------------------------------------------------------------
    //This is a useful method to generate a set of unique random numbers within a specified range, which can be 
    //used for testing or other purposes where uniqueness is important.
    public static int generateUniqueRandomNumber(int limit) {
        Random rand = new Random();
        HashSet<Integer> generatedNumbers = new HashSet<Integer>();
        int randomNum;
        do {
            randomNum = rand.nextInt(limit);
        } while (generatedNumbers.contains(randomNum));
        generatedNumbers.add(randomNum);
        return randomNum;
    }

    //--------------------------------------------------------------------------------------------------------------------------------
    public static void Test1() {
        //---------------------------------------------------
        // Get five random values from the test data 
        int randomValueTest1 = MyTestData[1][0];
        int randomValueTest2 = MyTestData[24][0];
        int randomValueTest3 = MyTestData[80][0];
        int randomValueTest4 = MyTestData[39][0];
        int randomValueTest5 = MyTestData[3][0];
        //--------------------------------------------------
        // Calculate page number and offset for each random value
        //--------------------------------------------------
        int pageNumber1 = calculatePageNumber(randomValueTest1);
        int offset1 = calculateOffset(randomValueTest1);
        //------------------------------------------
        int pageNumber2 = calculatePageNumber(randomValueTest2);
        int offset2 = calculateOffset(randomValueTest2);
        //------------------------------------------
        int pageNumber3 = calculatePageNumber(randomValueTest3);
        int offset3 = calculateOffset(randomValueTest3);
        //------------------------------------------
        int pageNumber4 = calculatePageNumber(randomValueTest4);
        int offset4 = calculateOffset(randomValueTest4);
        //------------------------------------------
        int pageNumber5 = calculatePageNumber(randomValueTest5);
        int offset5 = calculateOffset(randomValueTest5);
        //----------------------------------------------------
        // Initialize variables to store frame numbers
        int frame1 = 0;
        int frame2 = 0;
        int frame3 = 0;
        int frame4 = 0;
        int frame5 = 0;
        //----------------------------------------------------
        // Loop through the page table and get frame numbers for each page number
        for (int i = 0; i < pageTable.length; i++) {
            if (i == pageNumber1) {
                frame1 = pageTable[i];
            }
            if (i == pageNumber2) {
                frame2 = pageTable[i];
            }
            if (i == pageNumber3) {
                frame3 = pageTable[i];
            }
            if (i == pageNumber4) {
                frame4 = pageTable[i];
            }
            if (i == pageNumber5) {
                frame5 = pageTable[i];
            }
        }
        //-----------------------------------------
        // Calculate index for each random value and its corresponding frame
        int index1 = calculateIndex(frame1, offset1);
        int index2 = calculateIndex(frame2, offset2);
        int index3 = calculateIndex(frame3, offset3);
        int index4 = calculateIndex(frame4, offset4);
        int index5 = calculateIndex(frame5, offset5);
        //-----------------------------------------
        // Initialize message variable
        String massge = "";
        for (int i = 0; i < MyTestData.length; i++) {
            for (int j = 1; j < MyTestData[i].length; j++) {
                if (physicalMemory[index1] == MyTestData[i][j]) {
                    massge = "Yes";
                } else if (physicalMemory[index2] == MyTestData[i][j]) {
                    massge = "Yes";
                } else if (physicalMemory[index3] == MyTestData[i][j]) {
                    massge = "Yes";

                } else if (physicalMemory[index4] == MyTestData[i][j]) {
                    massge = "Yes";
                } else if (physicalMemory[index5] == MyTestData[i][j]) {
                    massge = "Yes";
                } else {
                    massge = "No";
                }
            }

        }
        //--------------------------------------------------------------------------------------------------------------------
        // This code section is responsible for printing the results of the five test cases in a formatted manner. 
        //It displays the logical address, page number, 
        //offset, frame number, value retrieved from physical memory, and whether the retrieved value matches the expected value.
        System.out.println("********************************************************************************************************************\n"
                + "                                                The Five Test Case                                           \n"
                + "********************************************************************************************************************\n"
                + "Logical Address\t\tPage #\t\toffset\t\tframe #\t\tvalue\t\tsame as model answer\n"
                + "--------------------------------------------------------------------------------------------------------------------");
        System.out.println(randomValueTest1 + "\t\t\t" + pageNumber1 + "\t\t" + offset1 + "\t\t" + frame1 + "\t\t" + physicalMemory[index1] + "\t\t" + massge);
        System.out.println(randomValueTest2 + "\t\t\t" + pageNumber2 + "\t\t" + offset2 + "\t\t" + frame2 + "\t\t" + physicalMemory[index2] + "\t\t" + massge);
        System.out.println(randomValueTest3 + "\t\t\t" + pageNumber3 + "\t\t" + offset3 + "\t\t" + frame3 + "\t\t" + physicalMemory[index3] + "\t\t" + massge);
        System.out.println(randomValueTest4 + "\t\t\t" + pageNumber4 + "\t\t" + offset4 + "\t\t" + frame4 + "\t\t" + physicalMemory[index4] + "\t\t" + massge);
        System.out.println(randomValueTest5 + "\t\t\t" + pageNumber5 + "\t\t" + offset5 + "\t\t" + frame5 + "\t\t" + physicalMemory[index5] + "\t\t" + massge);

    }//end method
    //-------------------------------------------------------------------------------------------------------------------
    // Statistics

    public static void Test2() {
        int count = 0;
        // Generate 30 unique random numbers and check if the corresponding page is in the page table
        for (int i = 0; i < pageTable.length; i++) {
            int index = generateUniqueRandomNumber(256);
            // If the page is in the page table and the valu !=-1,
            //then remove the page
            if (pageTable[index] != -1 && count < 30) {
                pageTable[index] = -1;
                count++;
            }
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        // Generate 80 logical addresses
        for (int i = 0; i < 80; i++) {
            int logicRndomAdrees = 0;
            for (int j = 0; j < 1; j++) {
                int index = generateUniqueRandomNumber(100);
                // Choose a random index from MyTestData and use it to select a logical address
                logicRndomAdrees = MyTestData[index][j];
            }
            list.add(logicRndomAdrees);
        }
        int countePageFlat = 0;
        int countePageHit = 0;
        int pageNumber = 0;
        // Count the number of page hits and page faults for the logical addresses
        for (int i = 0; i < list.size(); i++) {
            int logicAdress = list.get(i);
            pageNumber = calculatePageNumber(logicAdress);
            if (pageTable[pageNumber] == -1) {
                countePageFlat++;
            } else {
                countePageHit++;
            }

        }
        //-------------------------------------------------------
        // Print the results of the test
        System.out.println("\n********************************************************************************************************************");
        System.out.println("Counting number of page faults for a string of " + list.size() + " randomly chosen logical addresses: ");
        System.out.println("********************************************************************************************************************");
        System.out.println("The string sequence is: ");
        // Print the sequence of logical addresses
        for (int i = 0; i < list.size(); i++) {
            if (i % 10 == 0 && i != 0) {
                System.out.println("");
            }
            System.out.print(list.get(i) + ", ");
        }
        System.out.println("\n");
        System.out.println("> Number of page faults: " + countePageFlat);
        System.out.println("> Number of page hit: " + countePageHit);
    }

    //-------------------------------------------------------
    // This method calculates the page number of a logical address given a frame size
    private static int calculatePageNumber(int logicAdress) {
        // Divide the logical address by the frame size to get the page number
        int pageNumber = logicAdress / Framesize;
        // Return the page number
        return pageNumber;
    }

    //-------------------------------------------------------
    // This method calculates the offset of a logical address given a frame size
    private static int calculateOffset(int logicAdress) {
        // Calculate the remainder of the logical address divided by the frame size to get the offset    
        int offset = logicAdress % Framesize;
        // Return the offset 
        return offset;
    }

    //-------------------------------------------------------
    //Calculates the index of a memory access 
    //based on the frame number, frame size, and offset.
    private static int calculateIndex(int FramNumber, int offset) {
        int index = FramNumber * Framesize + offset;
        return index;
    }

    //-------------------------------------------------------
    //
    private static void PageReplacement(Scanner readCorrect) {
        // Initialize pageTable array with -1 values 
        for (int i = 0; i < pageTable.length; i++) {
            pageTable[i] = -1;
        }
        //Initialize physicalMemory array with -1 values
        for (int i = 0; i < physicalMemory128.length; i++) {
            physicalMemory128[i] = -1;
        }
        // generating 133 unique random page number from 
        //creating list with numbers from 0 to 132
        for (int i = 0; i < 133; i++) {
            rand_List133.add(i);
        }
        // Create a new Random object called rand to generate random numbers
        Random rand = new Random();
        // Loop through each element in the MyTestData2 array
        for (int i = 0; i < MyTestData2.length; i++) {
            values = readCorrect.nextByte();
            listValue.add(values);
            // Generate a logical random address by combining a page number and an offset value
            // using the rand_List133 ArrayList and the Framesize constant
            logicRandomAddres = (rand_List133.get(i) * Framesize) + rand.nextInt(256);
            // Set the first and second elements of the current row in the MyTestData2 array
            // to the generated logical random address and the corresponding byte value, respectively
            for (int j = 0; j < MyTestData2[i].length; j++) {
                MyTestData2[i][0] = logicRandomAddres;
                MyTestData2[i][1] = listValue.get(i);
            }

            //To mask the right most 16 bit of the logical address 
            logicRandomAddres = (int) (logicRandomAddres % Math.pow(2, 16));
            int pageNumber = calculatePageNumber(logicRandomAddres);

            // Calculate the offset from the logical random address
            offset = calculateOffset(logicRandomAddres);
            // Update the page table with the new page number and frame number
            for (int j = 0; j < pageTable.length; j++) {
                if (j == pageNumber) {
                    if (pageTable[j] == -1) {
                        FramNumber = generateUniqueRandomNumber(128);
                        pageTable[j] = FramNumber;
                    }
                }
            }
            // Calculate the index into the physical memory array
            index = calculateIndex(FramNumber, offset);
            listIndex.add(index);
        }
        // Copy the values from the listValue ArrayList into the physical memory array
        for (int k = 0; k < 128; k++) {
            physicalMemory128[listIndex.get(k)] = listValue.get(k);
        }
    }

    private static void Test3() {

        int reused1 = 0;
        int reused2 = 0;
        int reused3 = 0;
        int reused4 = 0;
        int reused5 = 0;
        //a loop that iterates through the pageTable array and checks for reused pages.
        //For each reused page, the code sets the corresponding index in the pageTable array to -1, and then 
        //iterates through the physicalMemory128 array and sets the corresponding frame for each reused page to -1. 
        for (int i = 0; i < pageTable.length; i++) {
            if (i == 0 && pageTable[i] != -1) {
                reused1 = pageTable[i];
            } else if (i == 1 && pageTable[i] != -1) {
                reused2 = pageTable[1];
            } else if (i == 2 && pageTable[i] != -1) {
                reused3 = pageTable[2];
            } else if (i == 3 && pageTable[i] != -1) {
                reused4 = pageTable[3];
            } else if (i == 4 && pageTable[i] != -1) {
                reused5 = pageTable[4];
            }
        }
        //initializes five indexes (indexnew1-indexnew5)
        //to be used when filling the physicalMemory128 array.
        int indexnew1 = 0;
        int indexnew2 = 0;
        int indexnew3 = 0;
        int indexnew4 = 0;
        int indexnew5 = 0;

        for (int i = 0; i < Framesize; i++) {
            indexnew1 = (reused1 * Framesize + i);
            physicalMemory128[indexnew1] = -1;
            indexnew2 = (reused2 * Framesize + i);
            physicalMemory128[indexnew2] = -1;
            indexnew3 = (reused3 * Framesize + i);
            physicalMemory128[indexnew3] = -1;
            indexnew4 = (reused4 * Framesize + i);
            physicalMemory128[indexnew4] = -1;
            indexnew5 = (reused5 * Framesize + i);
            physicalMemory128[indexnew5] = -1;
        }
        //initializes five different victumPage variables (victumPage1-victumPage5)
        //to be used when referencing the physicalMemory128 array.
        int victumPage1 = -1;
        int victumPage2 = -1;
        int victumPage3 = -1;
        int victumPage4 = -1;
        int victumPage5 = -1;
        //Loop through pageTable and 
        //assign index of matching reused value to corresponding variable. 
        // and save the index 
        for (int i = 0; i < pageTable.length; i++) {
            if (pageTable[i] == reused1) {
                pageTable[i] = -1;
                victumPage1 = i;
            }
            if (pageTable[i] == reused2) {
                pageTable[i] = -1;
                victumPage2 = i;
            }
            if (pageTable[i] == reused3) {
                pageTable[i] = -1;
                victumPage3 = i;
            }
            if (pageTable[i] == reused4) {
                pageTable[i] = -1;
                victumPage4 = i;
            }
            if (pageTable[i] == reused5) {
                pageTable[i] = -1;
                victumPage5 = i;
            }
        }

        int logicAdrees1 = 0, logicAdrees2 = 0, logicAdrees3 = 0, logicAdrees4 = 0, logicAdrees5 = 0;
        int NewPage1 = 0, off1 = 0, index1 = 0;
        int NewPage2 = 0, off2 = 0, index2 = 0;
        int NewPage3 = 0, off3 = 0, index3 = 0;
        int NewPage4 = 0, off4 = 0, index4 = 0;
        int NewPage5 = 0, off5 = 0, index5 = 0;

        /* Calculate the page number, offset, and index for the given logic address and store the value in the physical memory at the given index. Update the page table accordingly. */
        // int NewPage1 = calculatePageNumber(logicAdrees1); int off1 = calculateOffset(logicAdrees1); int index1 = calculateIndex(reused1, off1); physicalMemory128[index1] = listValue.get(128);
        //pageTable[NewPage1] = reused1;
        for (int i = 128; i < MyTestData2.length; i++) {
            if (i == 128) {
                logicAdrees1 = MyTestData2[128][0];
                NewPage1 = calculatePageNumber(logicAdrees1);
                off1 = calculateOffset(logicAdrees1);
                index1 = calculateIndex(reused1, off1);
                physicalMemory128[index1] = listValue.get(128);
                pageTable[NewPage1] = reused1;
            } else if (i == 129) {
                logicAdrees2 = MyTestData2[129][0];
                NewPage2 = calculatePageNumber(logicAdrees2);
                off2 = calculateOffset(logicAdrees2);
                index2 = calculateIndex(reused2, off2);
                pageTable[NewPage2] = reused2;
                physicalMemory128[index2] = listValue.get(129);
            } else if (i == 130) {
                logicAdrees3 = MyTestData2[130][0];
                NewPage3 = calculatePageNumber(logicAdrees3);
                off3 = calculateOffset(logicAdrees3);
                index3 = calculateIndex(reused3, off3);
                pageTable[NewPage3] = reused3;
                physicalMemory128[index3] = listValue.get(130);
            } else if (i == 131) {
                logicAdrees4 = MyTestData2[131][0];
                NewPage4 = calculatePageNumber(logicAdrees4);
                off4 = calculateOffset(logicAdrees4);
                index4 = calculateIndex(reused4, off4);
                pageTable[NewPage4] = reused4;
                physicalMemory128[index4] = listValue.get(131);
            } else if (i == 132) {
                logicAdrees5 = MyTestData2[132][0];
                NewPage5 = calculatePageNumber(logicAdrees5);
                off5 = calculateOffset(logicAdrees5);
                index5 = calculateIndex(reused5, off5);
                physicalMemory128[index5] = listValue.get(132);
                pageTable[NewPage5] = reused5;
            }

        }
        //prints out the results of the page replacement routine in a formatted table. The staement print shows the logical address, the new page number, the victim 
        //page number (if any), and the reused frame (if any). The code iterates over the five logical addresses, printing out the appropriate data for each one.
        System.out.println("\n\n********************************************************************************************************************\n"
                + "                              The testing of the page replacement routine is as follows:           \n"
                + "********************************************************************************************************************\n");
        System.out.println("Step1: running page replacement routine\n");
        System.out.printf("%20S%20s%20s%20s\n", "Logical Address: ", "new page#",
                "victim page #", "reused frame #");
        System.out.print("--------------------------------------------------------------------------------------\n");
        System.out.printf("%15s%20s%20s%20s     \n", logicAdrees1, NewPage1, victumPage1, reused1);
        System.out.printf("%15s%20s%20s%20s     \n", logicAdrees2, NewPage2, victumPage2, reused2);
        System.out.printf("%15s%20s%20s%20s     \n", logicAdrees3, NewPage3, victumPage3, reused3);
        System.out.printf("%15s%20s%20s%20s     \n", logicAdrees4, NewPage4, victumPage4, reused4);
        System.out.printf("%15s%20s%20s%20s     \n", logicAdrees5, NewPage5, victumPage5, reused5);

    }

    //--------------------------------------------------------------------------------------------------------------------------------
    public static void Test4() {
        //---------------------------------------------------
        // Get five random values from the test data 
        int randomValueTest1 = MyTestData2[128][0];
        int randomValueTest2 = MyTestData2[129][0];
        int randomValueTest3 = MyTestData2[130][0];
        int randomValueTest4 = MyTestData2[131][0];
        int randomValueTest5 = MyTestData2[132][0];
        //--------------------------------------------------
        // Calculate page number and offset for each random value
        //--------------------------------------------------
        int pageNumber1 = calculatePageNumber(randomValueTest1);
        int offset1 = calculateOffset(randomValueTest1);
        //------------------------------------------
        int pageNumber2 = calculatePageNumber(randomValueTest2);
        int offset2 = calculateOffset(randomValueTest2);
        //------------------------------------------
        int pageNumber3 = calculatePageNumber(randomValueTest3);
        int offset3 = calculateOffset(randomValueTest3);
        //------------------------------------------
        int pageNumber4 = calculatePageNumber(randomValueTest4);
        int offset4 = calculateOffset(randomValueTest4);
        //------------------------------------------
        int pageNumber5 = calculatePageNumber(randomValueTest5);
        int offset5 = calculateOffset(randomValueTest5);
        //----------------------------------------------------
        // Initialize variables to store frame numbers
        int frame1 = 0;
        int frame2 = 0;
        int frame3 = 0;
        int frame4 = 0;
        int frame5 = 0;
        //----------------------------------------------------
        // Loop through the page table and get frame numbers for each page number
        for (int i = 0; i < pageTable.length; i++) {
            if (i == pageNumber1) {
                frame1 = pageTable[i];
            }
            if (i == pageNumber2) {
                frame2 = pageTable[i];
            }
            if (i == pageNumber3) {
                frame3 = pageTable[i];
            }
            if (i == pageNumber4) {
                frame4 = pageTable[i];
            }
            if (i == pageNumber5) {
                frame5 = pageTable[i];
            }
        }
        //-----------------------------------------
        // Calculate index for each random value and its corresponding frame
        int index1 = calculateIndex(frame1, offset1);
        int index2 = calculateIndex(frame2, offset2);
        int index3 = calculateIndex(frame3, offset3);
        int index4 = calculateIndex(frame4, offset4);
        int index5 = calculateIndex(frame5, offset5);
        //-----------------------------------------
        // Initialize message variable
        String massge1;
        String massge2;
        String massge3;
        String massge4;
        String massge5;
        //This code checks if the values at the given indexes of physicalMemory128 
        //equal the values in MyTestData2, and assigns Yes or No strings to the 
        //massge1-5 variables accordingly.
        if (physicalMemory128[index1] == MyTestData2[128][1]) {
            massge1 = "Yes";
        } else {
            massge1 = "No";
        }

        if (physicalMemory128[index2] == MyTestData2[129][1]) {
            massge2 = "Yes";
        } else {
            massge2 = "No";
        }

        if (physicalMemory128[index3] == MyTestData2[130][1]) {
            massge3 = "Yes";
        } else {
            massge3 = "No";
        }
        if (physicalMemory128[index4] == MyTestData2[131][1]) {
            massge4 = "Yes";
        } else {
            massge4 = "No";
        }
        if (physicalMemory128[index5] == MyTestData2[132][1]) {
            massge5 = "Yes";
        } else {
            massge5 = "No";
        }

        //--------------------------------------------------------------------------------------------------------------------
        // This code section is responsible for printing the results of the five test cases in a formatted manner. 
        //It displays the logical address, page number, 
        //offset, frame number, value retrieved from physical memory, and whether the retrieved value matches the expected value.
        System.out.println("********************************************************************************************************************\n"
                + "                                 The testing of the page replacement routine is as follows:                                            \n"
                + "********************************************************************************************************************\n\n"
                + "Step2: the result of retrieving the values of 5 logical addresses:\n\n"
                + "Logical Address\t\tPage #\t\toffset\t\tframe #\t\tvalue\t\tsame as model answer\n"
                + "--------------------------------------------------------------------------------------------------------------------");
        System.out.println(randomValueTest1 + "\t\t\t" + pageNumber1 + "\t\t" + offset1 + "\t\t" + frame1 + "\t\t" + physicalMemory128[index1] + "\t\t" + massge1);
        System.out.println(randomValueTest2 + "\t\t\t" + pageNumber2 + "\t\t" + offset2 + "\t\t" + frame2 + "\t\t" + physicalMemory128[index2] + "\t\t" + massge2);
        System.out.println(randomValueTest3 + "\t\t\t" + pageNumber3 + "\t\t" + offset3 + "\t\t" + frame3 + "\t\t" + physicalMemory128[index3] + "\t\t" + massge3);
        System.out.println(randomValueTest4 + "\t\t\t" + pageNumber4 + "\t\t" + offset4 + "\t\t" + frame4 + "\t\t" + physicalMemory128[index4] + "\t\t" + massge4);
        System.out.println(randomValueTest5 + "\t\t\t" + pageNumber5 + "\t\t" + offset5 + "\t\t" + frame5 + "\t\t" + physicalMemory128[index5] + "\t\t" + massge5);

    }//end method

}
/*
Device Specifications:
compiler name and version:  NetBeans IDE 8.2


---------------------------------------
 */
