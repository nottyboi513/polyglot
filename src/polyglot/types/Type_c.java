/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2008 Polyglot project group, Cornell University
 * Copyright (c) 2006-2008 IBM Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program and the accompanying materials are made available under
 * the terms of the Lesser GNU Public License v2.0 which accompanies this
 * distribution.
 * 
 * The development of the Polyglot project has been supported by a
 * number of funding sources, including DARPA Contract F30602-99-1-0533,
 * monitored by USAF Rome Laboratory, ONR Grant N00014-01-1-0968, NSF
 * Grants CNS-0208642, CNS-0430161, and CCF-0133302, an Alfred P. Sloan
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/

package polyglot.types;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.types.Package;
import java.io.*;

/**
 * Abstract implementation of a <code>Type</code>. This implements most of the
 * "isa" and "cast" methods of the type and methods which just dispatch to the
 * type system.
 */
public abstract class Type_c extends TypeObject_c implements Type {
	/** Used for deserializing types. */
	protected Type_c() {
	}

	/** Creates a new type in the given a TypeSystem. */
	public Type_c(TypeSystem ts) {
		this(ts, null);
	}

	/** Creates a new type in the given a TypeSystem at a given position. */
	public Type_c(TypeSystem ts, Position pos) {
		super(ts, pos);
	}

	/**
	 * Return a string into which to translate the type.
	 * 
	 * @param c
	 *            A resolver in which to lookup this type to determine if the
	 *            type is unique in the given resolver.
	 */
	public abstract String translate(Resolver c);

	public boolean isType() {
		return true;
	}

	public boolean isPackage() {
		return false;
	}

	public Type toType() {
		return this;
	}

	public Package toPackage() {
		return null;
	}

	/* To be filled in by subtypes. */
	public boolean isCanonical() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isNumeric() {
		return false;
	}

	public boolean isIntOrLess() {
		return false;
	}

	public boolean isLongOrLess() {
		return false;
	}

	public boolean isVoid() {
		return false;
	}

	public boolean isBoolean() {
		return false;
	}

	public boolean isChar() {
		return false;
	}

	public boolean isByte() {
		return false;
	}

	public boolean isShort() {
		return false;
	}

	public boolean isInt() {
		return false;
	}

	public boolean isLong() {
		return false;
	}

	public boolean isFloat() {
		return false;
	}

	public boolean isDouble() {
		return false;
	}

	public boolean isReference() {
		return false;
	}

	public boolean isNull() {
		return false;
	}

	public boolean isClass() {
		return false;
	}

	public boolean isArray() {
		return false;
	}

	/**
	 * Return true if a subclass of Throwable.
	 */
	public boolean isThrowable() {
		return false;
	}

	/**
	 * Return true if an unchecked exception.
	 */
	public boolean isUncheckedException() {
		return false;
	}

	/** Returns a non-null iff isClass() returns true. */
	public ClassType toClass() {
		return null;
	}

	/** Returns a non-null iff isNull() returns true. */
	public NullType toNull() {
		return null;
	}

	/** Returns a non-null iff isReference() returns true. */
	public ReferenceType toReference() {
		return null;
	}

	/** Returns a non-null iff isPrimitive() returns true. */
	public PrimitiveType toPrimitive() {
		return null;
	}

	/** Returns a non-null iff isArray() returns true. */
	public ArrayType toArray() {
		return null;
	}

	/**
	 * Return a <code>dims</code>-array of this type.
	 */
	public ArrayType arrayOf(int dims) {
		return ts.arrayOf(this, dims);
	}

	/**
	 * Return an array of this type.
	 */
	public ArrayType arrayOf() {
		return ts.arrayOf(this);
	}

	public final boolean typeEquals(Type t) {
		return ts.typeEquals(this, t);
	}

	public boolean typeEqualsImpl(Type t) {
		return this == t;
	}

	/**
	 * Return true if this type is a subtype of <code>ancestor</code>.
	 */
	public final boolean isSubtype(Type t) {
		return ts.isSubtype(this, t);
	}

	/**
	 * Return true if this type is a subtype of <code>ancestor</code>.
	 */
	public boolean isSubtypeImpl(Type t) {
		return ts.typeEquals(this, t) || ts.descendsFrom(this, t);
	}

	/**
	 * Return true if this type descends from <code>ancestor</code>.
	 */
	public final boolean descendsFrom(Type t) {
		return ts.descendsFrom(this, t);
	}

	/**
	 * Return true if this type descends from <code>ancestor</code>.
	 */
	public boolean descendsFromImpl(Type t) {
		return false;
	}

	/**
	 * Return true if this type can be cast to <code>toType</code>.
	 */
	public final boolean isCastValid(Type toType) {
		return ts.isCastValid(this, toType);
	}

	/**
	 * Return true if this type can be cast to <code>toType</code>.
	 */
	public boolean isCastValidImpl(Type toType) {
		return false;
	}

	/**
	 * Return true if a value of this type can be assigned to a variable of type
	 * <code>toType</code>.
	 */
	public final boolean isImplicitCastValid(Type toType) {
		return ts.isImplicitCastValid(this, toType);
	}

	/**
	 * Return true if a value of this type can be assigned to a variable of type
	 * <code>toType</code>.
	 */
	public boolean isImplicitCastValidImpl(Type toType) {
		return false;
	}

	/**
	 * Return true if a literal <code>value</code> can be converted to this
	 * type. This method should be removed. It is kept for backward
	 * compatibility.
	 */
	public final boolean numericConversionValid(long value) {
		return ts.numericConversionValid(this, value);
	}

	/**
	 * Return true if a literal <code>value</code> can be converted to this
	 * type. This method should be removed. It is kept for backward
	 * compatibility.
	 */
	public boolean numericConversionValidImpl(long value) {
		return false;
	}

	/**
	 * Return true if a literal <code>value</code> can be converted to this
	 * type.
	 */
	public final boolean numericConversionValid(Object value) {
		return ts.numericConversionValid(this, value);
	}

	/**
	 * Return true if a literal <code>value</code> can be converted to this
	 * type.
	 */
	public boolean numericConversionValidImpl(Object value) {
		return false;
	}

	/**
	 * Return true if the types can be compared; that is, if they have the same
	 * type system.
	 */
	public boolean isComparable(Type t) {
		return t.typeSystem() == ts;
	}

	public abstract String toString();

	/**
	 * Output a compilable representation of this type to <code>w</code>. This
	 * implementation generates whatever representation is produced by
	 * <code>toString()</code>. To satisfy the specification of
	 * <code>Type.toString()</code>, this implementation needs to be overridden
	 * if <code>toString</code> does not produce a compilable representation.
	 */
	public void print(CodeWriter w) {
		w.write(toString());
	}
}
