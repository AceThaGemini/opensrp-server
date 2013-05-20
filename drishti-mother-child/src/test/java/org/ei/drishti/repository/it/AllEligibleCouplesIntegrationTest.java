package org.ei.drishti.repository.it;

import org.ei.drishti.domain.EligibleCouple;
import org.ei.drishti.domain.Mother;
import org.ei.drishti.repository.AllEligibleCouples;
import org.ei.drishti.repository.AllMothers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.ei.drishti.util.EasyMap.create;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-drishti.xml")
public class AllEligibleCouplesIntegrationTest {
    @Autowired
    private AllEligibleCouples eligibleCouples;
    @Autowired
    private AllMothers allMothers;

    @Before
    public void setUp() throws Exception {
        eligibleCouples.removeAll();
        allMothers.removeAll();
    }

    @Test
    public void shouldRegisterEligibleCouple() throws Exception {
        EligibleCouple couple = new EligibleCouple("CASE X", "EC Number 1").withCouple("Wife 1", "Husband 1").withANMIdentifier("ANM X").withDetails(create("Key 1", "Value 1").put("Key 2", "Value 2").map());

        eligibleCouples.register(couple);

        List<EligibleCouple> allCouplesInDB = eligibleCouples.getAll();
        assertThat(allCouplesInDB, is(asList(couple)));
        assertThat(allCouplesInDB.get(0).wife(), is("Wife 1"));
    }

    @Test
    public void shouldMarkAsCloseBothECAndMotherWhenECIsClosed() throws Exception {
        EligibleCouple couple1 = new EligibleCouple("CASE X", "EC Number 1").withCouple("Wife 1", "Husband 1").withANMIdentifier("ANM X");
        EligibleCouple couple2 = new EligibleCouple("CASE Y", "EC Number 2").withCouple("Wife 2", "Husband 2").withANMIdentifier("ANM X");
        EligibleCouple couple3 = new EligibleCouple("CASE Z", "EC Number 3");
        Mother mother1 = new Mother("CASE A", "CASE X", "Thayi 1").withAnm("ANM X").setIsClosed(false);
        Mother mother2 = new Mother("CASE B", "CASE Y", "Thayi 2").withAnm("ANM X").setIsClosed(false);
        eligibleCouples.register(couple1);
        eligibleCouples.register(couple2);
        eligibleCouples.register(couple3);
        allMothers.register(mother1);
        allMothers.register(mother2);
        assertThat(eligibleCouples.getAll(), is(asList(couple1, couple2, couple3)));
        assertThat(allMothers.getAll(), is(asList(mother1, mother2)));

        eligibleCouples.close("CASE X");
        eligibleCouples.close("CASE Z");

        assertThat(eligibleCouples.getAll(), is(asList(couple1.setIsClosed(true), couple2, couple3.setIsClosed(true))));
        assertThat(allMothers.getAll(), is(asList(mother1.setIsClosed(true), mother2)));
    }

    @Test
    public void shouldNotTryAndCloseANonExistingCouple() {
        EligibleCouple couple = new EligibleCouple("CASE X", "EC Number 1").withCouple("Wife 1", "Husband 1").withANMIdentifier("ANM X");
        eligibleCouples.register(couple);
        assertThat(eligibleCouples.getAll(), is(asList(couple)));

        eligibleCouples.close("THIS CASE DOES NOT EXIST");

        assertThat(eligibleCouples.getAll(), is(asList(couple)));
    }

    @Test
    public void shouldUpdateDetails() throws Exception {
        eligibleCouples.register(coupleWithoutDetails().withDetails(create("Key 1", "Value 1").put("Key 2", "Value 2").map()));
        EligibleCouple updatedCouple = eligibleCouples.updateDetails("CASE X", create("Key 2", "Value 2 NEW").put("Key 3", "Value 3").map());

        Map<String, String> expectedUpdatedDetails = create("Key 1", "Value 1").put("Key 2", "Value 2 NEW").put("Key 3", "Value 3").map();
        assertThat(eligibleCouples.findByCaseId("CASE X"), is(coupleWithoutDetails().withDetails(expectedUpdatedDetails)));
        assertThat(updatedCouple, is(coupleWithoutDetails().withDetails(expectedUpdatedDetails)));
    }

    private EligibleCouple coupleWithoutDetails() {
        return new EligibleCouple("CASE X", "EC Number 1").withCouple("Wife 1", "Husband 1").withANMIdentifier("ANM X").withLocation("Village 1", "SubCenter 1", "PHC 1");
    }
}
