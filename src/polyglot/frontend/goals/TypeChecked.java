/*
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2006 Polyglot project group, Cornell University
 * 
 */

/*
 * TypeChecked.java
 * 
 * Author: nystrom
 * Creation date: Oct 11, 2005
 */
package polyglot.frontend.goals;

import java.util.*;

import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.Scheduler;
import polyglot.types.TypeSystem;
import polyglot.visit.TypeChecker;

/**
 * a <code>TypeChecked</code> is reached after typechecking.
 */
public class TypeChecked extends VisitorGoal {
    public static Goal create(Scheduler scheduler, Job job, TypeSystem ts, NodeFactory nf) {
        return scheduler.internGoal(new TypeChecked(job, ts, nf));
    }

    protected TypeChecked(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, new TypeChecker(job, ts, nf));
    }

    public Collection prerequisiteGoals(Scheduler scheduler) {
        List l = new ArrayList();
        l.add(scheduler.Disambiguated(job));
        l.addAll(super.prerequisiteGoals(scheduler));
        return l;
    }

//    public Collection corequisiteGoals(Scheduler scheduler) {
//        List l = new ArrayList();
//        l.add(scheduler.ConstantsChecked(job));
//        l.addAll(super.corequisiteGoals(scheduler));
//        return l;
//    }
}
