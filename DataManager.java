package com.raywenderlich.quizzdroid;

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

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.javascript.JavaScriptReplicationFilterCompiler;
import com.couchbase.lite.javascript.JavaScriptViewCompiler;
import com.couchbase.lite.listener.Credentials;
import com.couchbase.lite.listener.LiteListener;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.util.ZipUtils;

import java.io.IOException;

public class DataManager {

  public Database database;

  private static DataManager instance = null;
  private Replication mPush;
  private Replication mPull;

  protected DataManager(Context context) {
    Manager manager = null;
    try{
      // Instantiates the Couchbase Lite Manager. A Mangager is the top-leel object managing a
      // collection of Couchbase Lite Database instances. You must create a Manager instance
      // before working with Couchbase Lite objects. The Manager.DEFAULT_OPTIONS parameter
      // indicates default options, including read/write support.
      manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);

      // Checks for an existing Database name "quizzdroid". This line returns null if the cata
      // does not exist, as would be the case on first launch.
      database = manager.getExistingDatabase("quizzdroid");

    }catch (IOException e){
      e.printStackTrace();
    }catch (CouchbaseLiteException e){
      e.printStackTrace();
    }

    // If the database does not exist, the ZipUtils.unzip method unzips the zip database file
    // into the files directory; otherwise, nothing furhter needs to be done.
    if (database == null){
      try {
        ZipUtils.unzip(context.getAssets().open("quizzdroid.cblite2.zip"), manager.getContext()
                .getFilesDir());
        // The database is instantiated using the getDataBase method.
        database = manager.getDatabase("quizzdroid");
      }catch (IOException e){
        e.printStackTrace();
      }catch (CouchbaseLiteException e){
        e.printStackTrace();
      }
    }

    View.setCompiler(new JavaScriptViewCompiler());
    Database.setFilterCompiler(new JavaScriptReplicationFilterCompiler());

    Credentials credentials = new Credentials(null, null);
    LiteListener liteListener = new LiteListener(manager, 5984, credentials);

    Thread thread = new Thread(liteListener);
    thread.start();
  }

  public static DataManager getSharedInstance(Context context) {
    if (instance == null) {
      instance = new DataManager(context);
    }
    return instance;
  }
}
