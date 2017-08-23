package com.wordblocks.gdx;

public class LetterBlock {
    public char c;
    public int h;
    public int id;
    public String word;

    public LetterBlock(LetterBlock other) {
        if (other != null) {
            this.c = other.c;
            this.h = other.h;
            this.word = other.word;
            this.id = other.id;
        }
    }
    public LetterBlock(){
    }
}
