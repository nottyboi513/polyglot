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

import polyglot.frontend.*;
import polyglot.frontend.goals.*;
import polyglot.types.*;
import polyglot.util.InternalCompilerError;
import java.util.Iterator;

/**
 * A LazyClassInitializer is responsible for initializing members of a class
 * after it has been created. Members are initialized lazily to correctly handle
 * cyclic dependencies between classes.
 */
public class DeserializedClassInitializer implements LazyClassInitializer {
	protected TypeSystem ts;
	protected ParsedClassType ct;
	protected boolean init;

	public DeserializedClassInitializer(TypeSystem ts) {
		this.ts = ts;
	}

	public void setClass(ParsedClassType ct) {
		this.ct = ct;
	}

	public boolean fromClassFile() {
		return false;
	}

	public void initTypeObject() {
		if (this.init)
			return;
		if (ct.isMember() && ct.outer() instanceof ParsedClassType) {
			ParsedClassType outer = (ParsedClassType) ct.outer();
			outer.addMemberClass(ct);
		}
		for (Iterator i = ct.memberClasses().iterator(); i.hasNext();) {
			ParsedClassType ct = (ParsedClassType) i.next();
			ct.initializer().initTypeObject();
		}
		this.init = true;
	}

	public boolean isTypeObjectInitialized() {
		return this.init;
	}

	public void initSuperclass() {
	}

	public void initInterfaces() {
	}

	public void initMemberClasses() {
	}

	public void initConstructors() {
	}

	public void initMethods() {
	}

	public void initFields() {
	}

	public void canonicalConstructors() {
	}

	public void canonicalMethods() {
	}

	public void canonicalFields() {
	}
}
