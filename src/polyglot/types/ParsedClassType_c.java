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

import java.io.*;
import java.util.*;

import polyglot.frontend.*;
import polyglot.frontend.goals.*;
import polyglot.main.Report;
import polyglot.types.*;
import polyglot.types.Package;
import polyglot.util.*;

/**
 * ParsedClassType
 * 
 * Overview: A ParsedClassType represents a information that has been parsed
 * (but not necessarily type checked) from a .java file.
 **/
public class ParsedClassType_c extends ClassType_c implements ParsedClassType {
	protected transient LazyClassInitializer init;
	protected transient Source fromSource;
	protected transient Job job;

	protected Type superType;
	protected List interfaces;
	protected List methods;
	protected List fields;
	protected List constructors;
	protected Package package_;
	protected Flags flags;
	protected Kind kind;
	protected String name;
	protected ClassType outer;

	protected transient List memberClasses;

	public LazyClassInitializer init() {
		return init;
	}

	public void setInit(LazyClassInitializer init) {
		this.init = init;
	}

	/** Was the class declared in a static context? */
	protected boolean inStaticContext = false;

	/** Wether we need to serialize this class. */
	protected boolean needSerialization = true;

	protected ParsedClassType_c() {
		super();
	}

	public ParsedClassType_c(TypeSystem ts, LazyClassInitializer init,
			Source fromSource) {
		super(ts);
		this.fromSource = fromSource;

		setInitializer(init);

		this.interfaces = new TypedList(new LinkedList(), Type.class, false);
		this.methods = new TypedList(new LinkedList(), MethodInstance.class,
				false);
		this.fields = new TypedList(new LinkedList(), FieldInstance.class,
				false);
		this.constructors = new TypedList(new LinkedList(),
				ConstructorInstance.class, false);
		this.memberClasses = new TypedList(new LinkedList(), Type.class, false);
	}

	public LazyInitializer initializer() {
		return this.init;
	}

	public void setInitializer(LazyInitializer init) {
		this.init = (LazyClassInitializer) init;
		((LazyClassInitializer) init).setClass(this);
	}

	public Source fromSource() {
		return fromSource;
	}

	public Job job() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Kind kind() {
		return kind;
	}

	public void inStaticContext(boolean inStaticContext) {
		this.inStaticContext = inStaticContext;
	}

	public boolean inStaticContext() {
		return inStaticContext;
	}

	public ClassType outer() {
		if (isTopLevel())
			return null;
		if (outer == null)
			throw new InternalCompilerError(
					"Nested classes must have outer classes.");

		return outer;
	}

	public String name() {
		// if (isAnonymous())
		// throw new
		// InternalCompilerError("Anonymous classes cannot have names.");
		//
		// if (name == null)
		// throw new
		// InternalCompilerError("Non-anonymous classes must have names.");
		return name;
	}

	/** Get the class's super type. */
	public Type superType() {
		init.initSuperclass();
		return this.superType;
	}

	/** Get the class's package. */
	public Package package_() {
		return package_;
	}

	/** Get the class's flags. */
	public Flags flags() {
		if (isAnonymous())
			return Flags.NONE;
		return flags;
	}

	public void setFlags(Flags flags) {
		this.flags = flags;
	}

	public void flags(Flags flags) {
		this.flags = flags;
	}

	public void kind(Kind kind) {
		this.kind = kind;
	}

	public void outer(ClassType outer) {
		if (isTopLevel())
			throw new InternalCompilerError(
					"Top-level classes cannot have outer classes.");
		this.outer = outer;
	}

	public void setContainer(ReferenceType container) {
		if (container instanceof ClassType && isMember()) {
			outer((ClassType) container);
		} else {
			throw new InternalCompilerError(
					"Only member classes can have containers.");
		}
	}

	public void name(String name) {
		if (isAnonymous())
			throw new InternalCompilerError(
					"Anonymous classes cannot have names.");
		this.name = name;
	}

	public void position(Position pos) {
		this.position = pos;
	}

	public void package_(Package p) {
		this.package_ = p;
	}

	public void superType(Type t) {
		this.superType = t;
	}

	public void addInterface(Type t) {
		interfaces.add(t);
	}

	public void addMethod(MethodInstance mi) {
		methods.add(mi);
	}

	public void addConstructor(ConstructorInstance ci) {
		constructors.add(ci);
	}

	public void addField(FieldInstance fi) {
		fields.add(fi);
	}

	public void addMemberClass(ClassType t) {
		memberClasses.add(t);
	}

	public void setInterfaces(List l) {
		this.interfaces = new ArrayList(l);
	}

	public void setMethods(List l) {
		this.methods = new ArrayList(l);
	}

	public void setFields(List l) {
		this.fields = new ArrayList(l);
	}

	public void setConstructors(List l) {
		this.constructors = new ArrayList(l);
	}

	public void setMemberClasses(List l) {
		this.memberClasses = new ArrayList(l);
	}

	public boolean defaultConstructorNeeded() {
		init.initConstructors();
		if (flags().isInterface()) {
			return false;
		}
		return this.constructors.isEmpty();
	}

	/** Return an immutable list of constructors */
	public List constructors() {
		init.initConstructors();
		init.canonicalConstructors();
		return Collections.unmodifiableList(constructors);
	}

	/** Return an immutable list of member classes */
	public List memberClasses() {
		init.initMemberClasses();
		return Collections.unmodifiableList(memberClasses);
	}

	/** Return an immutable list of methods. */
	public List methods() {
		init.initMethods();
		init.canonicalMethods();
		return Collections.unmodifiableList(methods);
	}

	/** Return a list of all methods with the given name. */
	public List methodsNamed(String name) {
		// Override to NOT call methods(). Do not check that all
		// methods are canonical, just that the particular method
		// returned is canonical.
		init.initMethods();

		List l = new LinkedList();

		for (Iterator i = methods.iterator(); i.hasNext();) {
			MethodInstance mi = (MethodInstance) i.next();
			if (mi.name().equals(name)) {
				if (!mi.isCanonical()) {
					// Force an exception to get thrown.
					init.canonicalMethods();
				}
				l.add(mi);
			}
		}

		return l;
	}

	/** Return an immutable list of fields */
	public List fields() {
		init.initFields();
		init.canonicalFields();
		return Collections.unmodifiableList(fields);
	}

	/** Get a field of the class by name. */
	public FieldInstance fieldNamed(String name) {
		// Override to NOT call fields(). Do not check that all
		// fields are canonical, just that the particular field
		// returned is canonical. This avoids an infinite loop
		// during disambiguation of path-dependent types like
		// in Jx or Jif.
		init.initFields();

		for (Iterator i = fields.iterator(); i.hasNext();) {
			FieldInstance fi = (FieldInstance) i.next();
			if (fi.name().equals(name)) {
				if (!fi.isCanonical()) {
					// Force an exception to get thrown.
					init.canonicalFields();
				}
				return fi;
			}
		}

		return null;
	}

	/** Return an immutable list of interfaces */
	public List interfaces() {
		init.initInterfaces();
		return Collections.unmodifiableList(interfaces);
	}

	protected boolean membersAdded;
	protected boolean supertypesResolved;
	protected boolean signaturesResolved;

	/**
	 * @return Returns the membersAdded.
	 */
	public boolean membersAdded() {
		return membersAdded;
	}

	/**
	 * @param membersAdded
	 *            The membersAdded to set.
	 */
	public void setMembersAdded(boolean membersAdded) {
		this.membersAdded = membersAdded;
	}

	/**
	 * @param signaturesDisambiguated
	 *            The signaturesDisambiguated to set.
	 */
	public void setSignaturesResolved(boolean signaturesDisambiguated) {
		this.signaturesResolved = signaturesDisambiguated;
	}

	/**
	 * @return Returns the supertypesResolved.
	 */
	public boolean supertypesResolved() {
		return supertypesResolved;
	}

	/**
	 * @param supertypesResolved
	 *            The supertypesResolved to set.
	 */
	public void setSupertypesResolved(boolean supertypesResolved) {
		this.supertypesResolved = supertypesResolved;
	}

	public int numSignaturesUnresolved() {
		Scheduler scheduler = typeSystem().extensionInfo().scheduler();

		if (signaturesResolved) {
			return 0;
		}

		if (!membersAdded()) {
			return Integer.MAX_VALUE;
		}

		// Create a new list of members. Don't use members() since
		// it ensures that signatures be resolved and this method
		// is just suppossed to check if they are resolved.
		List l = new ArrayList();
		l.addAll(methods);
		l.addAll(fields);
		l.addAll(constructors);
		l.addAll(memberClasses);

		int count = 0;

		for (Iterator i = l.iterator(); i.hasNext();) {
			MemberInstance mi = (MemberInstance) i.next();
			if (!mi.isCanonical()) {
				count++;
			}
		}

		if (count == 0) {
			signaturesResolved = true;
		}

		return count;
	}

	public boolean signaturesResolved() {
		if (!signaturesResolved) {
			if (!membersAdded()) {
				return false;
			}

			Scheduler scheduler = typeSystem().extensionInfo().scheduler();

			// Create a new list of members. Don't use members() since
			// it ensures that signatures be resolved and this method
			// is just suppossed to check if they are resolved.
			List l = new ArrayList();
			l.addAll(methods);
			l.addAll(fields);
			l.addAll(constructors);
			l.addAll(memberClasses);

			int count = 0;

			for (Iterator i = l.iterator(); i.hasNext();) {
				MemberInstance mi = (MemberInstance) i.next();
				if (!mi.isCanonical()) {
					if (Report.should_report("ambcheck", 2))
						Report.report(2, mi + " is ambiguous");
					count++;
				}
			}

			if (count == 0) {
				signaturesResolved = true;
			}
		}

		return signaturesResolved;
	}

	public String toString() {
		if (kind() == null) {
			return "<unknown class " + name + ">";
		}
		if (isAnonymous()) {
			if (interfaces != null && !interfaces.isEmpty()) {
				return "<anonymous subtype of " + interfaces.get(0) + ">";
			}
			if (superType != null) {
				return "<anonymous subclass of " + superType + ">";
			}
		}
		return super.toString();
	}

	/**
	 * When serializing, write out the place holder as well as the object
	 * itself. This should be done in TypeOutputStream, not here, but I couldn't
	 * get it working. --Nate
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		Object o = ts.placeHolder(this);
		if (o instanceof PlaceHolder && o != this) {
			out.writeBoolean(true);
			out.writeObject(o);
		} else {
			out.writeBoolean(false);
		}
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		if (in instanceof TypeInputStream) {
			TypeInputStream tin = (TypeInputStream) in;

			boolean b = tin.readBoolean();

			if (b) {
				tin.enableReplace(false);
				PlaceHolder p = (PlaceHolder) tin.readObject();
				tin.installInPlaceHolderCache(p, this);
				tin.enableReplace(true);
			}

			fromSource = null;
			job = null;

			init = tin.getTypeSystem().deserializedClassInitializer();
			init.setClass(this);

			membersAdded = true;
			supertypesResolved = true;
			signaturesResolved = true;
			memberClasses = new ArrayList();
		}

		in.defaultReadObject();
	}

	public void needSerialization(boolean b) {
		needSerialization = b;
	}

	public boolean needSerialization() {
		return needSerialization;
	}
}
