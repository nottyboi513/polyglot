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

package polyglot.ast;

import polyglot.util.*;
import polyglot.types.*;
import polyglot.visit.*;
import java.util.*;

/**
 * An <code>ArrayAccess</code> is an immutable representation of an access of an
 * array member.
 */
public class ArrayAccess_c extends Expr_c implements ArrayAccess {
	protected Expr array;
	protected Expr index;

	public ArrayAccess_c(Position pos, Expr array, Expr index) {
		super(pos);
		assert (array != null && index != null);
		this.array = array;
		this.index = index;
	}

	/** Get the precedence of the expression. */
	public Precedence precedence() {
		return Precedence.LITERAL;
	}

	/** Get the array of the expression. */
	public Expr array() {
		return this.array;
	}

	/** Set the array of the expression. */
	public ArrayAccess array(Expr array) {
		ArrayAccess_c n = (ArrayAccess_c) copy();
		n.array = array;
		return n;
	}

	/** Get the index of the expression. */
	public Expr index() {
		return this.index;
	}

	/** Set the index of the expression. */
	public ArrayAccess index(Expr index) {
		ArrayAccess_c n = (ArrayAccess_c) copy();
		n.index = index;
		return n;
	}

	/** Return the access flags of the variable. */
	public Flags flags() {
		return Flags.NONE;
	}

	/** Reconstruct the expression. */
	protected ArrayAccess_c reconstruct(Expr array, Expr index) {
		if (array != this.array || index != this.index) {
			ArrayAccess_c n = (ArrayAccess_c) copy();
			n.array = array;
			n.index = index;
			return n;
		}

		return this;
	}

	/** Visit the children of the expression. */
	public Node visitChildren(NodeVisitor v) {
		Expr array = (Expr) visitChild(this.array, v);
		Expr index = (Expr) visitChild(this.index, v);
		return reconstruct(array, index);
	}

	/** Type check the expression. */
	public Node typeCheck(TypeChecker tc) throws SemanticException {
		TypeSystem ts = tc.typeSystem();

		if (!array.type().isArray()) {
			throw new SemanticException(
					"Subscript can only follow an array type.", position());
		}

		if (!ts.isImplicitCastValid(index.type(), ts.Int())) {
			throw new SemanticException("Array subscript must be an integer.",
					position());
		}

		return type(array.type().toArray().base());
	}

	public Type childExpectedType(Expr child, AscriptionVisitor av) {
		TypeSystem ts = av.typeSystem();

		if (child == index) {
			return ts.Int();
		}

		if (child == array) {
			return ts.arrayOf(this.type);
		}

		return child.type();
	}

	public String toString() {
		return array + "[" + index + "]";
	}

	/** Write the expression to an output file. */
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		printSubExpr(array, w, tr);
		w.write("[");
		printBlock(index, w, tr);
		w.write("]");
	}

	public Term firstChild() {
		return array;
	}

	public List acceptCFG(CFGBuilder v, List succs) {
		v.visitCFG(array, index, ENTRY);
		v.visitCFG(index, this, EXIT);
		return succs;
	}

	public List throwTypes(TypeSystem ts) {
		return CollectionUtil.list(ts.OutOfBoundsException(),
				ts.NullPointerException());
	}

	public Node copy(NodeFactory nf) {
		return nf.ArrayAccess(this.position, this.array, this.index);
	}
}
