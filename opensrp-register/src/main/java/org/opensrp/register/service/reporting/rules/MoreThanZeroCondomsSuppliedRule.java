package org.opensrp.register.service.reporting.rules;

import org.opensrp.common.util.IntegerUtil;
import org.opensrp.service.reporting.rules.IRule;
import org.opensrp.util.SafeMap;
import org.springframework.stereotype.Component;

import static org.opensrp.common.AllConstants.FamilyPlanningFormFields.NUMBER_OF_CONDOMS_SUPPLIED_FIELD_NAME;

@Component
public class MoreThanZeroCondomsSuppliedRule implements IRule {

    @Override
    public boolean apply(SafeMap reportFields) {
        return IntegerUtil.tryParse(reportFields.get(NUMBER_OF_CONDOMS_SUPPLIED_FIELD_NAME), 0) > 0;
    }
}

