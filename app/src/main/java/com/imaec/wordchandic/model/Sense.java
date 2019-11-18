package com.imaec.wordchandic.model;

import org.simpleframework.xml.Element;

import java.io.Serializable;

@Element(name = "sense")
public class Sense implements Serializable {

    @Element
    int target_code;
    @Element
    int sense_no;
    @Element
    String definition;
    @Element(required = false)
    String pos = "";
    @Element
    String link;
    @Element
    String type;
    @Element(required = false)
    String cat = "없음";
    @Element(required = false)
    String origin = "";
    @Element(required = false)
    String syntacticArgument = "";
    @Element(required = false)
    String syntacticAnnotation= "";

    public int getTarget_code() {
        return target_code;
    }

    public void setTarget_code(int target_code) {
        this.target_code = target_code;
    }

    public int getSense_no() {
        return sense_no;
    }

    public void setSense_no(int sense_no) {
        this.sense_no = sense_no;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSyntacticArgument() {
        return syntacticArgument;
    }

    public void setSyntacticArgument(String syntacticArgument) {
        this.syntacticArgument = syntacticArgument;
    }

    public String getSyntacticAnnotation() {
        return syntacticAnnotation;
    }

    public void setSyntacticAnnotation(String syntacticAnnotation) {
        this.syntacticAnnotation = syntacticAnnotation;
    }
}
