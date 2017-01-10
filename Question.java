package com.raywenderlich.quizzdroid.model;

/*
 * Copyright (c) 2016 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.View;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

public class Question {
  private String _id;
  private String _rev;
  private String text;
  private String tag;
  private String type;
  private List<String> options;
  @JsonIgnore
  private String _attachments;


  public Question() {

  }

  public static Query getQuestions (Database database){
    // Request a database view named questions. This will create the view if it does not exist.
    View view = database.getView("app/questions");
    if (view.getMap() == null){
      // If the view does not already have a mapping, create one with a new Mapper object. The
      // second parameter to setMap is a version number. If your code for the map changes in the
      // future, the version number should be incremented as well.
      view.setMap(new Mapper() {
        @Override
        // The Mapper object calls map for each document in the database.
        public void map(Map<String, Object> document, Emitter emitter) {
          // If the document type equals question, then emit the key-value pair for the view.
          if (document.get("type").equals("question")){
            emitter.emit(document.get("_id"), null);
          }
        }
      }, "1");
    }
    Query query = view.createQuery();
    return query;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String get_rev() {
    return _rev;
  }

  public void set_rev(String _rev) {
    this._rev = _rev;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getOptions() {
    return options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public String get_attachments() {
    return _attachments;
  }

  public void set_attachments(String _attachments) {
    this._attachments = _attachments;
  }




}
