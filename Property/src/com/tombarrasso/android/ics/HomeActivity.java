/*
 * Copyright (C) 2011 Thomas James Barrasso <contact @ tombarrasso.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tombarrasso.android.ics;

// Android Packages
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

// Property
import android.util.Property;

/**
 * Example usage of the {@link Property} API in Android 4.0 ICS,
 * now supporting ANY Android version! It uses Reflection to
 * manipulate the {@link Field} or {@link Method} of a {@link Class}.
 *
 * @author	Thomas James Barrasso <contact @ tombarrasso.com>
 */

public class HomeActivity extends Activity
{
	// Random Object with a primitive Field.
	public static final class MyObject
	{
		public int mFoo;
	
		public final int getFoo()
		{
			return mFoo;
		}

		public final void setFoo(int mBar)
		{
			this.mFoo = mBar;
		}
	}

	private static final MyObject mObject = new MyObject();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final TextView mTV = (TextView) findViewById(R.id.text);

		// Example usage of the Android 4.0 ICS Property API.
		Property mProp;
		// Automatically finds the getFoo.
		mProp = Property.of(MyObject.class, Integer.class, "foo");
		mProp.set(mObject, 100);
		int hundred = (Integer) mProp.get(mObject);
		// Automatically finds the mFoo Field.
		mProp = Property.of(MyObject.class, Integer.class, "mFoo");
		mProp.set(mObject, 50);
		int fifty = (Integer) mProp.get(mObject);

		// Set the text of the TextView.
		mTV.setText("mObject.getFoo() was " + hundred + ", now mObject.mFoo == " + fifty + ".");
    }
}
