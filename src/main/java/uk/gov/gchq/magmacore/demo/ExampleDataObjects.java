/*
 * Copyright 2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.gov.gchq.magmacore.demo;

import static uk.gov.gchq.hqdm.iri.HQDM.ENTITY_NAME;
import static uk.gov.gchq.hqdm.services.SpatioTemporalExtentServices.event;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.USER_BASE;
import static uk.gov.gchq.magmacore.util.DataObjectUtils.uid;

import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import uk.gov.gchq.hqdm.iri.HQDM;
import uk.gov.gchq.hqdm.iri.IRI;
import uk.gov.gchq.hqdm.model.Association;
import uk.gov.gchq.hqdm.model.ClassOfStateOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.ClassOfStateOfPerson;
import uk.gov.gchq.hqdm.model.Event;
import uk.gov.gchq.hqdm.model.FunctionalSystem;
import uk.gov.gchq.hqdm.model.KindOfAssociation;
import uk.gov.gchq.hqdm.model.KindOfBiologicalSystemComponent;
import uk.gov.gchq.hqdm.model.KindOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.KindOfFunctionalSystemComponent;
import uk.gov.gchq.hqdm.model.KindOfPerson;
import uk.gov.gchq.hqdm.model.Participant;
import uk.gov.gchq.hqdm.model.Person;
import uk.gov.gchq.hqdm.model.PossibleWorld;
import uk.gov.gchq.hqdm.model.Role;
import uk.gov.gchq.hqdm.model.StateOfFunctionalSystem;
import uk.gov.gchq.hqdm.model.StateOfPerson;
import uk.gov.gchq.hqdm.model.Thing;
import uk.gov.gchq.hqdm.services.SpatioTemporalExtentServices;

/**
 * Constructs a set of example HQDM objects for demonstrating Magma Core.
 */
public final class ExampleDataObjects {

    private ExampleDataObjects() {}

    /**
     * Creates and populates a Jena dataset with the example data objects.
     *
     * @return The populated Jena dataset.
     */
    public static final Dataset buildDataset() {
        final Model model = ModelFactory.createDefaultModel();

        createDataObjects().forEach(object -> {
            final Resource resource = model.createResource(object.getId());
            object.getPredicates()
                    .forEach((iri, predicates) -> predicates.forEach(predicate -> resource
                            .addProperty(model.createProperty(iri.toString()),
                                    predicate.toString())));
        });
        return DatasetFactory.create(model);
    }

    /**
     * Generates a set of example data objects using HQDM.
     *
     * @return A list of HQDM objects.
     */
    public static List<Thing> createDataObjects() {
        final ModelBuilder builder = new ModelBuilder();

        // RDL CLASSES - Can be created, stored and queried separately.

        // Viewable is a class to assign other data objects to, to indicate that they are likely to
        // be of direct interest to a system user.
        final uk.gov.gchq.hqdm.model.Class viewable = builder.createClass("VIEWABLE");

        // A sub-set of the Viewable class.
        final uk.gov.gchq.hqdm.model.Class viewableObject = builder.createClass("VIEWABLE_OBJECT");

        // A sub-set of the Viewable Class for viewable Associations.
        final uk.gov.gchq.hqdm.model.Class viewableAssociation = builder.createClass("VIEWABLE_ASSOCIATION");

        // A system is composed of components so this is the class of components that a whole-life
        // person can have.
        final KindOfBiologicalSystemComponent kindOfBiologicalSystemHumanComponent =
                builder.createKindOfBiologicalSystemComponent("KIND_OF_BIOLOGICAL_SYSTEM_HUMAN_COMPONENT");

        // A class of whole-life person (re-)created as Reference Data.
        final KindOfPerson kindOfPerson = builder.createKindOfPerson("KIND_OF_PERSON");

        // A class of temporal part (state) of a (whole-life) person.
        final ClassOfStateOfPerson classOfStateOfPerson = builder.createClassOfStateOfPerson("CLASS_OF_STATE_OF_PERSON");

        // A class of whole-life system that is a Building.
        final KindOfFunctionalSystem kindOfFunctionalSystemBuilding =
                builder.createKindOfFunctionalSystem("KIND_OF_FUNCTIONAL_SYSTEM_BUILDING");

        // A Domestic Property is a system composed of components (e.g. walls, floors, roof, front
        // door, etc). This is the class of those whole-life system components.
        final KindOfFunctionalSystemComponent kindOfFunctionalSystemDomesticPropertyComponent =
                builder.createKindOfFunctionalSystemComponent("KIND_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY_COMPONENT");

        // The class of whole-life system that is domestic property.
        final KindOfFunctionalSystem kindOfFunctionalSystemDomesticProperty =
                builder.createKindOfFunctionalSystem("KIND_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY");

        // The class of state of system whose members are temporal parts of domestic properties.
        final ClassOfStateOfFunctionalSystem classOfStateOfFunctionalSystemDomesticProperty =
                builder.createClassOfStateOfFunctionalSystem("STATE_OF_FUNCTIONAL_SYSTEM_DOMESTIC_PROPERTY");

        // The class of role that every member of class of person plays.
        final Role personRole = builder.createRole("NATURAL_MEMBER_OF_SOCIETY_ROLE");

        // The class of role that every member of class of domestic property plays.
        final Role domesticPropertyRole = builder.createRole("ACCEPTED_PLACE_OF_SEMI_PERMANENT_HABITATION_ROLE");

        final Role domesticOccupantInPropertyRole = builder.createRole("DOMESTIC_PROPERTY_THAT_IS_OCCUPIED_ROLE");
        // Would be good to add part_of_by_class_(occupantInPropertyKindOfAssociation) but can't
        // neatly do that in the class as it can only be added after
        // occupantInPropertyKindOfAssociation is created. This can be added later for completeness.

        final Role occupierOfPropertyRole = builder.createRole("OCCUPIER_LOCATED_IN_PROPERTY_ROLE");
        // Would be good to add part_of_by_class_(occupantInPropertyKindOfAssociation) but can't
        // neatly do that in the class as it can only be added after
        // occupantInPropertyKindOfAssociation is created. This can be added later for completeness.

        // Add the Association Types (Participants and Associations).
        final KindOfAssociation occupantInPropertyKindOfAssociation =
                builder.createKindOfAssociation("OCCUPANT_LOCATED_IN_VOLUME_ENCLOSED_BY_PROPERTY_ASSOCIATION");

        builder.addSubclass(viewable, viewableObject);
        builder.addSubclass(viewable, viewableAssociation);
        builder.addSubclass(kindOfFunctionalSystemBuilding, kindOfFunctionalSystemDomesticProperty);
        builder.addSubclass(domesticPropertyRole, domesticOccupantInPropertyRole);
        builder.addSubclass(classOfStateOfPerson, occupierOfPropertyRole);

        builder.addClassMember(viewableObject, kindOfPerson);
        builder.addClassMember(viewableObject, classOfStateOfPerson);
        builder.addClassMember(viewableObject, kindOfFunctionalSystemDomesticProperty);
        builder.addClassMember(viewableObject, classOfStateOfFunctionalSystemDomesticProperty);
        builder.addClassMember(viewableAssociation, occupantInPropertyKindOfAssociation);

        builder.addHasComponentByClass(kindOfPerson, kindOfBiologicalSystemHumanComponent);
        builder.addHasComponentByClass(kindOfFunctionalSystemDomesticProperty, kindOfFunctionalSystemDomesticPropertyComponent);

        builder.addConsistsOfByClass(occupantInPropertyKindOfAssociation, domesticOccupantInPropertyRole);
        builder.addConsistsOfByClass(occupantInPropertyKindOfAssociation, occupierOfPropertyRole);

        // STATES

        // The main state: This is a mandatory component of all datasets if we are to stick to the
        // commitments in HQDM. This is the least strict treatment, the creation of a single
        // possible world.
        final PossibleWorld possibleWorld = builder.createPossibleWorld("Example1_World");

        // Person B Whole Life Object.
        final Event e1 = builder.createPointInTime("1991-02-18T00:00:00");
        builder.addToPossibleWorld(possibleWorld, e1);

        final Person personB1 = builder.createPerson("PersonB1_Bob");

        builder.addMemberOfKind(personB1, kindOfPerson);
        builder.addNaturalRole(personB1, personRole);
        builder.addToPossibleWorld(possibleWorld, personB1);
        builder.addBeginningEvent(personB1, e1);





        // Person B states.
        final Event e2 = event(new IRI(USER_BASE, "2020-08-15T17:50:00").toString());
        e2.addStringValue(HQDM.ENTITY_NAME.getIri(), "2020-08-15T17:50:00");
        e2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        final Event e3 = event(new IRI(USER_BASE, "2020-08-15T19:21:00").toString());
        e3.addStringValue(HQDM.ENTITY_NAME.getIri(), "2020-08-15T19:21:00");
        e3.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        final StateOfPerson personBs1 = SpatioTemporalExtentServices.createStateOfPerson(new IRI(USER_BASE, uid()).toString());

        personBs1.addValue(HQDM.MEMBER_OF.getIri(), classOfStateOfPerson.getId());
        personBs1.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        personBs1.addValue(HQDM.TEMPORAL_PART_OF.getIri(), personB1.getId());
        personBs1.addValue(HQDM.BEGINNING.getIri(), e2.getId());
        personBs1.addValue(HQDM.ENDING.getIri(), e3.getId());
        objects.add(e2);
        objects.add(e3);
        objects.add(personBs1);

        final Event e4 = event(new IRI(USER_BASE, "2020-08-16T22:33:00").toString());
        e4.addStringValue(HQDM.ENTITY_NAME.getIri(), "2020-08-16T22:33:00");
        e4.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        final Event e5 = event(new IRI(USER_BASE, "2020-08-17T10:46:00").toString());
        e5.addStringValue(HQDM.ENTITY_NAME.getIri(), "2020-08-17T10:46:00");
        e5.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        final StateOfPerson personBs2 = SpatioTemporalExtentServices.createStateOfPerson(new IRI(USER_BASE, uid()).toString());

        personBs2.addValue(HQDM.MEMBER_OF.getIri(), classOfStateOfPerson.getId());
        personBs2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        personBs2.addValue(HQDM.TEMPORAL_PART_OF.getIri(), personB1.getId());
        personBs2.addValue(HQDM.BEGINNING.getIri(), e4.getId());
        personBs2.addValue(HQDM.ENDING.getIri(), e5.getId());
        objects.add(e4);
        objects.add(e5);
        objects.add(personBs2);

        // House B Whole Life Object.
        final Event e6 = event(new IRI(USER_BASE, "1972-06-01T00:00:00").toString());
        e6.addStringValue(HQDM.ENTITY_NAME.getIri(), "1972-06-01T00:00:00");
        e6.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        final FunctionalSystem houseB = SpatioTemporalExtentServices.createFunctionalSystem(new IRI(USER_BASE, uid()).toString());

        houseB.addValue(HQDM.MEMBER_OF_KIND.getIri(), kindOfFunctionalSystemDomesticProperty.getId());
        houseB.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        houseB.addValue(HQDM.INTENDED_ROLE.getIri(), domesticPropertyRole.getId());
        houseB.addValue(HQDM.BEGINNING.getIri(), e6.getId());
        objects.add(e6);
        objects.add(houseB);

        // States of house when Occupant personBs1 is present.
        final StateOfFunctionalSystem houseBs1 = SpatioTemporalExtentServices.createStateOfFunctionalSystem(
                new IRI(USER_BASE, uid()).toString());

        houseBs1.addValue(HQDM.MEMBER_OF.getIri(), classOfStateOfFunctionalSystemDomesticProperty.getId());
        houseBs1.addValue(HQDM.TEMPORAL_PART_OF.getIri(), houseB.getId());
        houseBs1.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        houseBs1.addValue(HQDM.BEGINNING.getIri(), e2.getId());
        houseBs1.addValue(HQDM.ENDING.getIri(), e3.getId());
        objects.add(houseBs1);

        final StateOfFunctionalSystem houseBs2 = SpatioTemporalExtentServices.createStateOfFunctionalSystem(new IRI(USER_BASE, uid()).toString());

        houseBs2.addValue(HQDM.MEMBER_OF.getIri(), classOfStateOfFunctionalSystemDomesticProperty.getId());
        houseBs2.addValue(HQDM.TEMPORAL_PART_OF.getIri(), houseB.getId());
        houseBs2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        houseBs2.addValue(HQDM.BEGINNING.getIri(), e4.getId());
        houseBs2.addValue(HQDM.ENDING.getIri(), e5.getId());
        objects.add(houseBs2);

        // Add the Associations and map the states above to the appropriate participant objects.
        // If we had full has_superClass resolving in HQDM classes then this participant object
        // wouldn't be needed as the class occupierOfPropertyRole is also a sub-type of
        // state_of_person (see issues list).
        final Participant pPersonBs1 = SpatioTemporalExtentServices.createParticipant(new IRI(USER_BASE, uid()).toString());

        pPersonBs1.addValue(HQDM.MEMBER_OF_KIND.getIri(), occupierOfPropertyRole.getId());
        pPersonBs1.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        pPersonBs1.addValue(HQDM.TEMPORAL__PART_OF.getIri(), personBs1.getId());
        pPersonBs1.addValue(HQDM.BEGINNING.getIri(), e2.getId());
        pPersonBs1.addValue(HQDM.ENDING.getIri(), e3.getId());
        pPersonBs1.addStringValue(ENTITY_NAME.getIri(),
                "Note this is the state of person Bs1 that is participating the association");
        objects.add(pPersonBs1);

        final Participant pHouseBs1 = SpatioTemporalExtentServices.createParticipant(new IRI(USER_BASE, uid()).toString());

        pHouseBs1.addValue(HQDM.MEMBER_OF_KIND.getIri(), domesticOccupantInPropertyRole.getId());
        pHouseBs1.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        pHouseBs1.addValue(HQDM.TEMPORAL__PART_OF.getIri(), houseBs1.getId());
        pHouseBs1.addValue(HQDM.BEGINNING.getIri(), e2.getId());
        pHouseBs1.addValue(HQDM.ENDING.getIri(), e3.getId());
        pHouseBs1.addStringValue(ENTITY_NAME.getIri(),
                "Note this is the state of houseBs1 that is participating in the association");
        objects.add(pHouseBs1);

        final Participant pPersonBs2 = SpatioTemporalExtentServices.createParticipant(new IRI(USER_BASE, uid()).toString());

        pPersonBs2.addValue(HQDM.MEMBER_OF_KIND.getIri(), occupierOfPropertyRole.getId());
        pPersonBs2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        pPersonBs2.addValue(HQDM.TEMPORAL__PART_OF.getIri(), personBs2.getId());
        pPersonBs2.addValue(HQDM.BEGINNING.getIri(), e4.getId());
        pPersonBs2.addValue(HQDM.ENDING.getIri(), e5.getId());
        pPersonBs2.addStringValue(ENTITY_NAME.getIri(),
                "Note this is the state of person Bs2 that is participating in the association");
        objects.add(pPersonBs2);

        final Participant pHouseBs2 = SpatioTemporalExtentServices.createParticipant(new IRI(USER_BASE, uid()).toString());

        pHouseBs2.addValue(HQDM.MEMBER_OF_KIND.getIri(), domesticOccupantInPropertyRole.getId());
        pHouseBs2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        pHouseBs2.addValue(HQDM.TEMPORAL__PART_OF.getIri(), houseBs2.getId());
        pHouseBs2.addValue(HQDM.BEGINNING.getIri(), e4.getId());
        pHouseBs2.addValue(HQDM.ENDING.getIri(), e5.getId());
        pHouseBs2.addStringValue(ENTITY_NAME.getIri(),
                "Note this is the state of houseBs2 that is participating in the association");
        objects.add(pHouseBs2);

        final Association houseOccupantPresentState1 =
                SpatioTemporalExtentServices.createAssociation(new IRI(USER_BASE, uid()).toString());

        houseOccupantPresentState1.addValue(HQDM.MEMBER_OF_KIND.getIri(), occupantInPropertyKindOfAssociation.getId());
        houseOccupantPresentState1.addValue(HQDM.CONSISTS_OF_PARTICIPANT.getIri(), pHouseBs1.getId());
        houseOccupantPresentState1.addValue(HQDM.CONSISTS_OF_PARTICIPANT.getIri(), pPersonBs1.getId());
        houseOccupantPresentState1.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        houseOccupantPresentState1.addValue(HQDM.BEGINNING.getIri(), e2.getId());
        houseOccupantPresentState1.addValue(HQDM.ENDING.getIri(), e3.getId());
        // Abbreviated to allow a string to be displayed against this class of 'relationship'.
        houseOccupantPresentState1.addStringValue(ENTITY_NAME.getIri(), "HouseOccupantPresent1");
        objects.add(houseOccupantPresentState1);

        final Association houseOccupantPresentState2 =
                SpatioTemporalExtentServices.createAssociation(new IRI(USER_BASE, uid()).toString());

        houseOccupantPresentState2.addValue(HQDM.MEMBER_OF_KIND.getIri(), occupantInPropertyKindOfAssociation.getId());
        houseOccupantPresentState2.addValue(HQDM.CONSISTS_OF_PARTICIPANT.getIri(), pHouseBs2.getId());
        houseOccupantPresentState2.addValue(HQDM.CONSISTS_OF_PARTICIPANT.getIri(), pPersonBs2.getId());
        houseOccupantPresentState2.addValue(HQDM.PART_OF_POSSIBLE_WORLD.getIri(), possibleWorld.getId());
        houseOccupantPresentState2.addValue(HQDM.BEGINNING.getIri(), e4.getId());
        houseOccupantPresentState2.addValue(HQDM.ENDING.getIri(), e5.getId());
        // Abbreviated to allow a string to be displayed against this class of 'relationship'.
        houseOccupantPresentState2.addStringValue(ENTITY_NAME.getIri(), "HouseOccupantPresent2");
        objects.add(houseOccupantPresentState2);

        return objects;
    }
}
