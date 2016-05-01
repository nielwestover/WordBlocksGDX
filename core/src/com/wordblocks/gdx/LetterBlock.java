package com.wordblocks.gdx;

/**
 * Created by a2558 on 4/9/2016.
 */
public class LetterBlock{

    public LetterBlock(LetterBlock other) {
        if (other == null)
            return;
        this.c = other.c; // you can access
        this.h = other.h;
        this.word = other.word;
        this.id = other.id;
    }
    public LetterBlock(){}
    public char c;
    public int h;
    public String word;
    public int id;
}