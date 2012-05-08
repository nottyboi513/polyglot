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

import polyglot.util.*;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.types.Package;
import java.util.*;

/**
 * A <code>CachingResolver</code> memoizes another Resolver
 */
public class CachingResolver implements Resolver, Copy {
	protected Resolver inner;
	private Map cache;
	private boolean cacheNotFound;

	/**
	 * Create a caching resolver.
	 * 
	 * @param inner
	 *            The resolver whose results this resolver caches.
	 */
	public CachingResolver(Resolver inner, boolean cacheNotFound) {
		this.inner = inner;
		this.cacheNotFound = cacheNotFound;
		this.cache = new HashMap();
	}

	public CachingResolver(Resolver inner) {
		this(inner, true);
	}

	protected boolean shouldReport(int level) {
		return (Report.should_report("sysresolver", level) && this instanceof SystemResolver)
				|| Report.should_report(TOPICS, level);
	}

	public Object copy() {
		try {
			CachingResolver r = (CachingResolver) super.clone();
			r.cache = new HashMap(this.cache);
			return r;
		} catch (CloneNotSupportedException e) {
			throw new InternalCompilerError("clone failed");
		}
	}

	/**
	 * The resolver whose results this resolver caches.
	 */
	public Resolver inner() {
		return this.inner;
	}

	public String toString() {
		return "(cache " + inner.toString() + ")";
	}

	protected Collection cachedObjects() {
		return cache.values();
	}

	/**
	 * Find a type object by name.
	 * 
	 * @param name
	 *            The name to search for.
	 */
	public Named find(String name) throws SemanticException {
		if (shouldReport(2))
			Report.report(2, "CachingResolver: find: " + name);

		Object o = cache.get(name);

		if (o instanceof SemanticException)
			throw ((SemanticException) o);

		Named q = (Named) o;

		if (q == null) {
			if (shouldReport(3))
				Report.report(3, "CachingResolver: not cached: " + name);

			try {
				q = inner.find(name);
			} catch (NoClassException e) {
				if (shouldReport(3)) {
					Report.report(3, "CachingResolver: " + e.getMessage());
					Report.report(3, "CachingResolver: installing " + name
							+ "-> (not found) in resolver cache");
				}
				if (cacheNotFound) {
					cache.put(name, e);
				}
				throw e;
			}

			addNamed(name, q);

			if (shouldReport(3))
				Report.report(3, "CachingResolver: loaded: " + name);
		} else {
			if (shouldReport(3))
				Report.report(3, "CachingResolver: cached: " + name);
		}

		return q;
	}

	/**
	 * Check if a type object is in the cache, returning null if not.
	 * 
	 * @param name
	 *            The name to search for.
	 */
	public Named check(String name) {
		Object o = cache.get(name);
		if (o instanceof Throwable)
			return null;
		return (Named) o;
	}

	/**
	 * Install a qualifier in the cache.
	 * 
	 * @param name
	 *            The name of the qualifier to insert.
	 * @param q
	 *            The qualifier to insert.
	 */
	public void install(String name, Named q) {
		if (shouldReport(3))
			Report.report(3, "CachingResolver: installing " + name + "->" + q
					+ " in resolver cache");
		if (shouldReport(5))
			new Exception().printStackTrace();

		cache.put(name, q);
	}

	/**
	 * Install a qualifier in the cache.
	 * 
	 * @param name
	 *            The name of the qualifier to insert.
	 * @param q
	 *            The qualifier to insert.
	 */
	public void addNamed(String name, Named q) throws SemanticException {
		install(name, q);
	}

	public void dump() {
		Report.report(1, "Dumping " + this);
		for (Iterator i = cache.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			Report.report(2, e.toString());
		}
	}

	private static final Collection TOPICS = CollectionUtil.list(Report.types,
			Report.resolver);
}
