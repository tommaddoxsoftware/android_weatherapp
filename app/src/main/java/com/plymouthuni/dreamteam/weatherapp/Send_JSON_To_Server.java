//**************************************************************************//
// Nigel's amazing asynchronous send a JSON object to a URL.                ..
//                                                                          //
// You will see that I am not using the "JavaDoc" style of comments, as this//
// program isn't intended to be self-documenting.  When you write your code,//
// I suggest that you do indeed use the "JavaDoc" type comments.			//
// 																			//
// Uses HTTP POST or HTTP GET to send.                                      //
//																			//
// © Nigel@soc.plymouth.ac.uk 2017.											//
//**************************************************************************//
package com.plymouthuni.dreamteam.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;





//**************************************************************************//
// Starts here...                                                           //
//**************************************************************************//
public class Send_JSON_To_Server
{

    //**********************************************************************//
    // Enum to represent the HTTP mode, currently only POST and GET         //
    // supported.                                                           //
    //**********************************************************************//
    public enum HTTPMode {POST, GET};


    //**********************************************************************//
    // Constructor.                                                         //
    // Parameters are:                                                      //
    // url           = a URL to send to of course;                          //
    // parameterName = the name given to the parameter in HTTP POST. This   //
    //                 will more than likely be null with post (though I    //
    //                 sometimes use named parameters for POST with JSP.    //
    //                 This parameter can be null.                          //
    // jsonObject    = A JSON object.                                       //
    // httpMode      = Currently only supports HTTP POST and GET.           //
    // listener      = a listener to listen out for the HTTP response.  A   //
    //                 call-back will be sent.   Can be null.               //
    //                                                                      //
    // The result is returned in the main Thread by  the calling object     //
    // implementing the interface I_JSON_Response_Listener. Look at the     //
    // interface and you will see that a JSON object is returned.  The      //
    // attributes of that object will depend on your web service, but the   //
    // returned JSON object will always contain an attribute "result".      //
    //                                                                      //
    // If all works "result" will have the value "OK".                      //
    // If the thread can't connect a JSON object {result:connect failed} is //
    // returned.                                                            //
    // *********************************************************************//
    public Send_JSON_To_Server(String                   url,
                               String                   parameterName,
                               JSONObject               jsonObject,
                               HTTPMode                 httpMode,
                               I_JSON_Response_Listener listener)
    {
        this.strUrl        = url;
        this.parameterName = parameterName;
        this.jsonObject    = jsonObject;
        this.httpMode      = httpMode;
        this.listener      = listener;
        activityName       = getClass().getSimpleName();
    }







    //*********************************************************************//
    // Instance variables.												   //
    //*********************************************************************//
    private String                      strUrl        = null;
    private String                      activityName  = null;
    private String                      parameterName = null;
    private String                      responseStr   = null;
    private long                        waitTimeMS    = 5;
    private JSONObject                  jsonObject    = null;
    private String                      data          = null;
    private HTTPMode                    httpMode      = HTTPMode.GET;
    private I_JSON_Response_Listener    listener      = null;
    private WebServiceTask              task          = null;


    //******************************************************************//
    // Make this look a little like a Thread; start it with a start().  //
    //******************************************************************//
    public void start()
    {
        //**************************************************************//
        // We seem, on some devices only, eo get weird synchronous      //
        // errors here.  I think it is because the AsyncTask has a      //
        // handler, and we are creating the AsyncTask from within       //
        // another Thread.                                              //
        //**************************************************************//
        try
        {
            task = new WebServiceTask();
            task.execute();
        }
        catch (Exception wtf)
        {
            JSONObject err = new JSONObject();
            try
            {
                Log.e(activityName, "Create WS task JSON: "
                            + wtf.getMessage());
                err.put("result", "WS task: "
                        + wtf.getMessage());
                listener.onHTTPResponseReceived(err);
            }
            catch (JSONException jse)
            {
                Log.e(activityName, "Create WS task JSON: " + jse.getMessage());
            }
        }
    }


    //******************************************************************//
    // Do most of the work here.  It is actually invoked from my        //
    // AsyncTask.
    // This is synchronized, as a lazy solution to threading issues.    //
    //******************************************************************//
    private synchronized void runConnection()
    {
        HttpURLConnection con         = null;

        try
        {
            String jsonStr = null;
            //String jsonStr = URLEncoder.encode(toSendObject.toString(), "UTF-8");
            if (jsonObject != null)
                jsonStr = jsonObject.toString();



            //****************************************************************//
            // If using HTTP GET only.                                        //
            // Add the ? to the URL, and the word "add" and our JSON encoded  //
            // data.  We concatenate ?paramname followed by a JSON encoded    //
            // object to the URL.										      //
            //****************************************************************//
            if (httpMode == HTTPMode.GET)
                strUrl = strUrl + "?" + parameterName + "=" + jsonStr;
            URL url = new URL(strUrl);

            Log.i(activityName, "Connecting to " + url.toString());

            //********************************************************//
            // Create an HTTP connection, with various attributes.	  //
            //********************************************************//
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);		//Timeout in milliseconds.
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            switch (httpMode)
            {                   // Set the HTTP mode, of course.
                case GET:
                     con.setRequestMethod("GET");
                     break;

                case POST:
                     con.setRequestMethod("POST");
                     break;
            }

            //con.setChunkedStreamingMode(0);	//Default PHP doesn't like this one
            con.setDoInput(true);				//So that we can read response back
            con.setDoOutput(true);
            con.connect();

            BufferedWriter out = null;      // Used for POST.

            switch (httpMode)
            {
                case POST:
                     out = new BufferedWriter(
                         new OutputStreamWriter(
                                  con.getOutputStream()));

                    if (jsonStr != null)
                    {
                        if (parameterName != null)
                        {
                            String toSend = parameterName + "=" + jsonStr;
                            Log.i(activityName, "POST " + toSend);
                            out.write (toSend + "\r\n");
                            out.flush();
                        }
                        else
                        {
                            out.write(jsonStr);
                            out.flush();
                        }
                    }


                    if (data != null)
                    {
                        if (parameterName != null)
                        {
                            String p1 = parameterName + "=";
                            out.write(p1 + data);
                        } else out.write(data);
                        out.flush();
                        out.close();
                    }
                    break;  // End of case.


                case GET:
                     // Nothing to do, by the look of it.
                     break;
            }


            //********************************************************//
            // Now read the response.								  //
            //********************************************************//
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));


            //******************************************************************//
            // Read a line at a time...    Keep going until we find something   //
            // that looks like a JSON object, e.g. the "{".                     //
            //******************************************************************//
            Thread.sleep(waitTimeMS);

            String tmpResponse = null;
            do
            {
                tmpResponse = in.readLine();
                try
                {
                    if (tmpResponse.contains("{"))
                        responseStr = tmpResponse;

                    Log.i(activityName, "Read from server " + tmpResponse);
                }
                catch (Exception ee)
                {

                }
            }
            while (tmpResponse != null);

            in.close();
            if (out != null) out.close();
            con.disconnect();
        }

        //******************************************************************//
        // That was it.  The rest is just exceptions.					   //
        //******************************************************************//
        catch (Exception e)
        {
            Log.e(activityName, "IOException", e);

            JSONObject jo = new JSONObject();
            try
            {
                jo.put("result", "connect failed: "
                        + e.getMessage());
                responseStr = jo.toString();
            }
            catch (JSONException e1) { }    // Nothing to do.
        }
        finally
        {
            if (con != null)
            {
                con.disconnect();
            }
        }
        Log.i(activityName, "Run finished");
    } // runConnection



    //**********************************************************************//
    // Set the waiting time in ms.                                          //
    //**********************************************************************//
    public void setWaitTimeMS(long waitTimeMS)
    {
        this.waitTimeMS = waitTimeMS;
    }

    //**********************************************************************//
    // Return a JSON object; if there is an error then a JSON object with   //
    // the error encoded in it.                                             //
    //**********************************************************************//
    private void returnJSONObject()
    {
        if (listener != null)
        {
            JSONObject result = null;
            try
            {
                //**********************************************************//
                // Strip off any leading junk before the "{". Also any junk //
                // after the "}".                                           //
                //**********************************************************//
                int bracketStart = responseStr.indexOf('{');
                int bracketEnd   = responseStr.indexOf('}')+1;
                responseStr      = responseStr.substring(bracketStart, bracketEnd);
                result           = new JSONObject(responseStr);

                //**********************************************************//
                // Indicate that it all went swimmingly.                    //
                //**********************************************************//
                result.put("result", "OK");
            }
            catch (Exception e)
            {
                result = new JSONObject();
                try
                {
                    result.put("result", "Error "+ e.getMessage());
                }
                catch (JSONException e1)
                {
                    Log.e(activityName, "Can't put");
                }
            }
            listener.onHTTPResponseReceived(result);
        }
    }


    //**********************************************************************//
    // This is an inner class for an AsyncTask.  This gets over handling the//
    // web service as a separate thread, whine at the same time updating the//
    // UI components from within the original UI Thread.					//
    //																		//
    // Using an inner class means that the inner class is in the same scope //
    // as the outer class and can see its UI components.					//
    // 																		//
    // The parameters within the template are weird; we must use Java's 	//
    // classes Integer, Void, not the built in int, void etc.				//
    //**********************************************************************//
    private class WebServiceTask extends AsyncTask<Void, Void, Void>
    {

        //******************************************************************//
        // Executed in a separate thread in the background					//
        // Weird syntax for "some parameters may follow, but I don't know   //
        // anything about them".... 										//
        //******************************************************************//
        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i("WebServiceTask", "Task invoked");
            runConnection();
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            returnJSONObject();
        }

    } //End of AsyncTask



}   // End of classy class.
