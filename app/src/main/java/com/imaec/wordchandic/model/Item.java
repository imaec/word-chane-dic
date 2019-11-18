package com.imaec.wordchandic.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.ArrayList;

@Element(name = "item")
public class Item implements Serializable {

    @Element
    String word;
    @ElementList(inline = true)
    public ArrayList<Sense> listSense;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Sense> getListSense() {
        return listSense;
    }

    public void setListSense(ArrayList<Sense> listSense) {
        this.listSense = listSense;
    }
}
