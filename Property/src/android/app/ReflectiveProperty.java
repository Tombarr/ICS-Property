/*
 * Copyright (C) 2011 The Android Open Source Project
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

package android.util;

// Java Packages
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveProperty<T, V> extends Property<T, V>
{
	private static final String PREFIX_GET = "get";
	private static final String PREFIX_IS = "is";
	private static final String PREFIX_SET = "set";
	private Field mField;
	private Method mGetter;
	private Method mSetter;

	private static final Class[] EMPTY_CLASS_ARRAY = new Class[] {};
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

	public ReflectiveProperty(Class<T> type, Class<V> type2, String name)
	{
		super(type2, name);
		char c = Character.toUpperCase(name.charAt(0));
		String mStr1 = name.substring(1);
		String mStr2 = c + mStr1;
		String mStr = "get" + mStr2;
		Class<?> mReturnClass = null;
		try
		{
			this.mGetter = type.getMethod(mStr, EMPTY_CLASS_ARRAY);
			mReturnClass = this.mGetter.getReturnType();
			if (!typesMatch(type2, mReturnClass))
			{
				StringBuilder mStrBuilder = new StringBuilder().
					append("Underlying type (").append(mReturnClass).append(") ").
					append("does not match Property type (").append(type).append(")");
				throw new NoSuchPropertyException(mStrBuilder.toString());
			}
		}
		catch (NoSuchMethodException noMethod)
		{
			mStr = "is" + mStr2;
			try
			{
				this.mGetter = type.getMethod(mStr, EMPTY_CLASS_ARRAY);
			}
			catch (NoSuchMethodException noMethod2)
			{
				try
				{
					this.mField = type.getField(name);
					Class<?> mFieldClass = this.mField.getType();
					if (typesMatch(type2, mFieldClass)) return;

					StringBuilder mStrBuilder2 = new StringBuilder().
						append("Underlying type (").append(mFieldClass).append(") ").
						append("does not match Property type (").append(type2).append(")");
					throw new NoSuchPropertyException(mStrBuilder2.toString());
				}
				catch (NoSuchFieldException noField)
				{
					StringBuilder mStrBuilder3 = new StringBuilder().
						append("No accessor method or field found for property with name ").append(name);
					throw new NoSuchPropertyException(mStrBuilder3.toString());
				}
			}
		}

		String mSetStr = "set" + mStr2;
		try
		{
			Class[] mClassArray = new Class[1];
			mClassArray[0] = mReturnClass;
			this.mSetter = type.getMethod(mSetStr, mClassArray);
			return;
		}
		catch (NoSuchMethodException localNoSuchMethodException3) {	}
	}

	private boolean typesMatch(Class<V> paramClass, Class<?> paramClass1)
	{
		if (paramClass1 != paramClass)
		{
			if (paramClass1.isPrimitive())
			{
				// Check for matches, if one found keep going
				// because it will return true further down.
				if (!((paramClass1 == Float.TYPE) && (paramClass == Float.class)))
				{
					if (!((paramClass1 == Integer.TYPE) && (paramClass == Integer.class)))
					{
						if (!((paramClass1 == Boolean.TYPE) && (paramClass == Boolean.class)))
						{
							if (!((paramClass1 == Long.TYPE) && (paramClass == Long.class)))
							{
								if (!((paramClass1 == Double.TYPE) && (paramClass == Double.class)))
								{
									if (!((paramClass1 == Short.TYPE) && (paramClass == Short.class)))
									{
										if (!((paramClass1 == Byte.TYPE) && (paramClass == Byte.class)))
										{
											if (!((paramClass1 == Character.TYPE) && (paramClass == Character.class)))
											{
												return false;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	public V get(T object)
	{
		Object mObject = null;

		if (this.mGetter != null)
		{
			try
			{
				mObject = this.mGetter.invoke(object, EMPTY_OBJECT_ARRAY);
				return (V) mObject;
			}
			catch (IllegalAccessException localIllegalAccessException1)
			{
				throw new AssertionError();
			}
			catch (InvocationTargetException localInvocationTargetException)
			{
				Throwable localThrowable = localInvocationTargetException.getCause();
				throw new RuntimeException(localThrowable);
			}
		}

		if (this.mField != null)
		{
			try
			{
				mObject = this.mField.get(object);
				return (V) mObject;
			}
			catch (IllegalAccessException localIllegalAccessException2)
			{
				throw new AssertionError();
			}
		}

		throw new AssertionError();
	}

	public boolean isReadOnly()
	{
		return ((this.mSetter == null) && (this.mField == null));
	}

	public void set(T object, V value)
	{
		if (this.mSetter != null)
		{
			try
			{
				Object[] mObjectArray = new Object[1];
				mObjectArray[0] = value;
				this.mSetter.invoke(object, mObjectArray);
				return;
			}
			catch (IllegalAccessException localIllegalAccessException1)
			{
				throw new AssertionError();
			}
			catch (InvocationTargetException localInvocationTargetException)
			{
				Throwable localThrowable = localInvocationTargetException.getCause();
				throw new RuntimeException(localThrowable);
			}
		}

		if (this.mField != null)
		{
			try
			{
				this.mField.set(object, value);
				return;
			}
			catch (IllegalAccessException localIllegalAccessException2)
			{
				throw new AssertionError();
			}
		}

		StringBuilder mStr = new StringBuilder().append("Property ").
			append(getName()).append(" is read-only");

		throw new UnsupportedOperationException(mStr.toString());
	}
}
