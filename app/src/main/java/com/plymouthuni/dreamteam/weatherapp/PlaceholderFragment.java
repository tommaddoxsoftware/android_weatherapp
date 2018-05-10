
package com.plymouthuni.dreamteam.weatherapp;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//**********************************************************************//
// A dummy fragment representing a section of the app, but that simply  //
// displays dummy text.							                        //
//																		//
// As things scale, you will probably want to make these fragments		//
// separate classes.													//
//																		//
// NOTE from the imports that this uses the 							//
// android.support.v4.app.Fragment component, which (supposedly) gives 	//
// better support for backwards compatibility.							//
//                                                                      //
// By Nigel@soc.plymouth.ac.uk.                                         //
//**********************************************************************//
public class PlaceholderFragment extends Fragment
{

    //******************************************************************//
    // The fragment argument representing the section number for this	//
    // fragment.														//
    // 																	//
    // Ugh, a static variable as well.  It is just a key used to look up//
    // a value and never changes.										//
    //******************************************************************//
    public static String TitleKey         = "sec_title";
    private       String activityName     = "Dummy fragment";






    //******************************************************************//
    // And we have to override CreateView, a little like an old Activity//
    // except that we use an inflater to create the view.				//
    //																	//
    // a Fragment does have an onCreate() like an Activity, but Google	//
    // encourage us to do things this way...							//
    //																	//
    //******************************************************************//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle                   savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_placeholder,
                container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);

        String  title = getArguments().getString(TitleKey);
        tv.setText(title);

        return rootView;
    }



    //**********************************************************************//
    // On Pause call-back.  Paused != stopped.							   	//
    // If we are to avoid flattening the battery, we must remove any        //
    // location listeners, broadcast receivers and such.                    //
    //**********************************************************************//
    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(activityName, "Paused");
    }



    //**********************************************************************//
    // On Resume call-back.  Start the location service listeners.  You must//
    // understand the state diagram of an Activity at this point.			//
    //**********************************************************************//
    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(activityName, "Resumed");
    }






    //***********************************************************************//
    // On Stop call-back.  Pairs with On resume   Paused != stopped.		 //
    // Once we are stopped we are destroyable; we can be trashed without     //
    // warning, so we should preserve state if we need to.                   //
    //***********************************************************************//
    @Override
    public void onStop()
    {
        super.onStop();
        Log.i(activityName, "Stopped");
    }




    //***********************************************************************//
    // On Destroy call-back.  We are never usually terminated by Android; we //
    // usually just go into a stopped state until we are resumed.			 //
    //***********************************************************************//
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(activityName, "Destroyed");
    }

}	// End of classy class.






