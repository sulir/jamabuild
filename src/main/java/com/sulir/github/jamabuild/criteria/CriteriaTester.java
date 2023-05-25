package com.sulir.github.jamabuild.criteria;

import com.sulir.github.jamabuild.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CriteriaTester {
    private static final Logger log = LoggerFactory.getLogger(CriteriaTester.class);

    private final List<Criterion> criteria;
    private final Project project;

    public CriteriaTester(List<Criterion> criteria, Project project) {
        this.criteria = criteria;
        this.project = project;
    }

    public boolean anyMatches() {
        for (Criterion criterion : criteria) {
            if (criterion.isMet(project)) {
                log.info("Exclusion criterion " + criterion.getClass().getSimpleName() + " met, deleting project");
                project.delete();
                return true;
            }
        }

        return  false;
    }
}
