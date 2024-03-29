package project.Model.crypto.algorithm;

import project.Model.crypto.component.DataBlock;
import project.Model.crypto.component.KeyBlock;
import project.Model.patterns.Tables;
import project.Model.castTypes.Converter;

import project.Model.functions.XOR;
import project.Model.functions.Permutation;

public class Des {
    private DataBlock dataBlock;
    private KeyBlock keyBlock;

    public byte[] encrypt(byte[] byteText, byte[] byteKey){
        //konwertujemy block na 64bity
        byte[] binaryText = Converter.byteTabTo8bTab(byteText);
        //konwersja key na 64bit
        byte[] binaryKey = Converter.byteTabTo8bTab(byteKey);

        keyBlock = new KeyBlock(binaryKey);
        byte[][] keys = keyBlock.getLastPodklucz();
        dataBlock = new DataBlock(binaryText);

        //pobierz lewą i prawą stronę
        byte[] left = dataBlock.getLeft();
        byte[] right = dataBlock.getRight();

        byte[] expandedRightSite = new byte[48];

        byte[] finalForm = new byte[64];


        for (int i = 0; i < 16; i++) {
            //Expanding permutation (EP)
            expandedRightSite = Permutation.Permutation(Tables.EP,right,48);
            //pobierz i'ty podklucz
            byte[] key = keys[i];
            expandedRightSite = XOR.XOR(expandedRightSite,key);
            expandedRightSite = Permutation.sBoxTransformation(expandedRightSite);
            //Straight Permutation (SP)
            expandedRightSite = Permutation.Permutation(Tables.P, expandedRightSite,32);
            expandedRightSite = XOR.XOR(left,expandedRightSite);
            left = right;
            right = expandedRightSite;
        }

        System.arraycopy(right, 0, finalForm, 0, 32);
        System.arraycopy(left, 0, finalForm, 32, 32);

        //odwracamy początkową permutację
        finalForm = Permutation.Permutation(Tables.IP1,finalForm,64);

        //binarna tablica na byte tablicę
        byte[] toByte = Converter.binaryChainToByteForm(finalForm);

        return toByte;
    }


    public byte[] decrypt(byte[] byteText, byte[] byteKey) {
        //konwertujemy block na 64bity
        byte[] binaryText = Converter.byteTabTo8bTab(byteText);
        //konwersja key na 64bit
        byte[] binaryKey = Converter.byteTabTo8bTab(byteKey);

        keyBlock = new KeyBlock(binaryKey);
        byte[][] keys = keyBlock.getLastPodklucz();
        dataBlock = new DataBlock(binaryText);

        //pobierz lewą i prawą stronę
        byte[] left = dataBlock.getLeft();
        byte[] right = dataBlock.getRight();

        byte[] expandedRightSite = new byte[48];

        byte[] finalForm = new byte[64];

        for (int i = 15; i >= 0; i--) {
            expandedRightSite = Permutation.Permutation(Tables.EP,right,48);
            byte[] key = keys[i];

            //to co przy encrypt
            expandedRightSite = XOR.XOR(expandedRightSite,key);
            expandedRightSite = Permutation.sBoxTransformation(expandedRightSite);
            expandedRightSite = Permutation.Permutation(Tables.P, expandedRightSite,32);
            expandedRightSite = XOR.XOR(left,expandedRightSite);

            left = right;
            right = expandedRightSite;
        }

        System.arraycopy(right, 0, finalForm, 0, 32);
        System.arraycopy(left, 0, finalForm, 32, 32);

        finalForm = Permutation.Permutation(Tables.IP1,finalForm,64);

        byte[] toByte = Converter.binaryChainToByteForm(finalForm);

        return toByte;
    }
}
