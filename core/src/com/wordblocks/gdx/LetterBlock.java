package com.wordblocks.gdx;

public class LetterBlock {
    public char f83c;
    public int f84h;
    public int id;
    public String word;

    public LetterBlock(LetterBlock other) {
        if (other != null) {
            this.f83c = other.f83c;
            this.f84h = other.f84h;
            this.word = other.word;
            this.id = other.id;
        }
    }
    public LetterBlock(){
    }
}
