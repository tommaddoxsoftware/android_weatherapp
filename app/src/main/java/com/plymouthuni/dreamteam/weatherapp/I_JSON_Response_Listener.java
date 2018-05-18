//**************************************************************************//
// Nigel's attempt at a Web Services.										//
//																			//
// A simple HTTP response received listener interface.						//
//																			//
// You will see that I am not using the "JavaDoc" style of comments, as this//
// program isn't intended to be self-documenting.  When you write your code,//
// I suggest that you do indeed use the "JavaDoc" type comments.			//
// 																			//
// © Nigel@soc.plymouth.ac.uk 2016.											//
//**************************************************************************//
package com.plymouthuni.dreamteam.weatherapp;


import org.json.JSONObject;

public interface I_JSON_Response_Listener
{
    //**********************************************************************//
    // On Response Received. Although this application has Treads all over  //
    // the place, the intention is that this call-back will occur in the    //
    // main Thread.                                                         //
    //**********************************************************************//
    public void onHTTPResponseReceived(JSONObject response);



}	// End if interesting interface.
