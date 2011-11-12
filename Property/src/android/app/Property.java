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

/**
 * A property is an abstraction that can be used to represent a mutable
 * value that is held in a host object. The Property's set(Object, Object)
 * or get(Object) methods can be implemented in terms of the private fields
 * of the host object, or via "setter" and "getter" methods or by some other
 * mechanism, as appropriate.
 */

public abstract class Property<T, V>
{
	private final String mName;
	private final Class<V> mType;

	/**
	 * A constructor that takes an identifying name
	 * and type for the property.
	 */
	public Property(Class<V> type, String name)
	{
		this.mName = name;
		this.mType = type;
	}

	/**
	 * This factory method creates and returns a Property given
	 * the class and name parameters, where the "name" parameter represents either:
	 *
	 * <ul>
	 * 	<li>a public getName() method on the class which takes no arguments, plus an optional public setName() method which takes a value of the same type returned by getName()</li>
	 *	<li>a public isName() method on the class which takes no arguments, plus an optional public setName() method which takes a value of the same type returned by isName()</li>
	 * 	<li>a public name field on the class</li>
	 * </ul>
	 *
	 * If either of the get/is method alternatives is found on the class,
	 * but an appropriate setName() method is not found, the Property will be readOnly.
	 */
	public static <T, V> Property<T, V> of(Class<T> hostType, Class<V> valueType, String name)
	{
		return new ReflectiveProperty(hostType, valueType, name);
	}

	/**
	 * Returns the current value that this property
	 * represents on the given object.
	 */
	public abstract V get(T paramT);

	/**
	 * Returns the name for this property.
	 */
	public String getName()
	{
		return this.mName;
	}

	/**
	 * Returns the type for this property.
	 */
	public Class<V> getType()
	{
		return this.mType;
	}

	/**
	 * Returns true if the set(Object, Object) method does not set
	 * the value on the target object (in which case the set() method
	 * should throw a NoSuchPropertyException exception).
	 */
	public boolean isReadOnly()
	{
		return false;
	}

	/**
	 * Sets the value on object which this property represents.
	 */
	public void set(T paramT, V paramV)
	{
		StringBuilder mStr = new StringBuilder().append("Property ").
			append(getName()).append("is read-only");
		throw new UnsupportedOperationException(mStr.toString());
	}
}
