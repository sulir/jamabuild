package com.github.sulir.jamabuild.filtering;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.Settings;
import com.github.sulir.jamabuild.building.BuildResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProjectFilter {
    private static final Logger log = LoggerFactory.getLogger(ProjectFilter.class);

    private final List<Criterion> criteria = new ArrayList<>();

    public static ProjectFilter load(Settings settings) {
        ProjectFilter filter = new ProjectFilter();
        filter.add(loadCriteria(settings.preInclude(), Criterion.Phase.PRE_BUILD, Criterion.Type.INCLUDE));
        filter.add(loadCriteria(settings.preExclude(), Criterion.Phase.PRE_BUILD, Criterion.Type.EXCLUDE));
        filter.add(loadCriteria(settings.postInclude(), Criterion.Phase.POST_BUILD, Criterion.Type.INCLUDE));
        filter.add(loadCriteria(settings.postExclude(), Criterion.Phase.POST_BUILD, Criterion.Type.EXCLUDE));
        return filter;
    }

    private static List<Criterion> loadCriteria(String[] criteriaSettings, Criterion.Phase phase, Criterion.Type type) {
        List<Criterion> criteria = new ArrayList<>();

        for (String criterionText : criteriaSettings) {
            CriterionFactory factory = new CriterionFactory(phase, type);
            try {
                criteria.add(factory.createCriterion(criterionText));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return criteria;
    }

    public void add(List<Criterion> criteria) {
        this.criteria.addAll(criteria);
    }

    public boolean exclusionMatches(Project project, Criterion.Phase phase, BuildResult result) {
        for (Criterion criterion : criteria) {
            if (criterion.getPhase() != phase)
                continue;

            boolean includeNotMet = criterion.getType() == Criterion.Type.INCLUDE && !criterion.isMet(project, result);
            if (includeNotMet) {
                deleteProject(project, criterion);
                return true;
            }

            boolean excludeMet = criterion.getType() == Criterion.Type.EXCLUDE && criterion.isMet(project, result);
            if (excludeMet) {
                deleteProject(project, criterion);
                return true;
            }
        }

        return  false;
    }

    private void deleteProject(Project project, Criterion criterion) {
        log.info("Excluding and deleting project due to criterion {}", criterion.getClass().getSimpleName());
        project.delete();
    }
}
