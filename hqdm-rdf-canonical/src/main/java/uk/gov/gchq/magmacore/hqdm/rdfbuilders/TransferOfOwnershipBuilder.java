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

package uk.gov.gchq.magmacore.hqdm.rdfbuilders;

import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.AGGREGATED_INTO;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.BEGINNING;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CAUSES;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CAUSES_BEGINNING;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CAUSES_ENDING;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CONSISTS_OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CONSISTS_OF_PARTICIPANT;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CONSISTS_OF_PARTICIPANT_;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.CONSISTS__OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.DETERMINES;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.ENDING;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.MEMBER_OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.MEMBER_OF_KIND;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.MEMBER__OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.PART_OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.PART_OF_;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.PART_OF_POSSIBLE_WORLD;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.PART__OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.REFERENCES;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.TEMPORAL_PART_OF;
import static uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM.TEMPORAL__PART_OF;

import uk.gov.gchq.magmacore.hqdm.exception.HqdmException;
import uk.gov.gchq.magmacore.hqdm.model.Activity;
import uk.gov.gchq.magmacore.hqdm.model.AgreementExecution;
import uk.gov.gchq.magmacore.hqdm.model.Asset;
import uk.gov.gchq.magmacore.hqdm.model.BeginningOfOwnership;
import uk.gov.gchq.magmacore.hqdm.model.Class;
import uk.gov.gchq.magmacore.hqdm.model.ClassOfSociallyConstructedActivity;
import uk.gov.gchq.magmacore.hqdm.model.EndingOfOwnership;
import uk.gov.gchq.magmacore.hqdm.model.Event;
import uk.gov.gchq.magmacore.hqdm.model.ExchangeOfGoodsAndMoney;
import uk.gov.gchq.magmacore.hqdm.model.Individual;
import uk.gov.gchq.magmacore.hqdm.model.KindOfActivity;
import uk.gov.gchq.magmacore.hqdm.model.PossibleWorld;
import uk.gov.gchq.magmacore.hqdm.model.SpatioTemporalExtent;
import uk.gov.gchq.magmacore.hqdm.model.Thing;
import uk.gov.gchq.magmacore.hqdm.model.TransferOfOwnership;
import uk.gov.gchq.magmacore.hqdm.model.Transferee;
import uk.gov.gchq.magmacore.hqdm.model.Transferor;
import uk.gov.gchq.magmacore.hqdm.rdf.iri.IRI;
import uk.gov.gchq.magmacore.hqdm.rdfservices.RdfSpatioTemporalExtentServices;

/**
 * Builder for constructing instances of TransferOfOwnership.
 */
public class TransferOfOwnershipBuilder {

    private final TransferOfOwnership transferOfOwnership;

    /**
     * Constructs a Builder for a new TransferOfOwnership.
     *
     * @param iri IRI of the TransferOfOwnership.
     */
    public TransferOfOwnershipBuilder(final IRI iri) {
        transferOfOwnership = RdfSpatioTemporalExtentServices.createTransferOfOwnership(iri.getIri());
    }

    /**
     * A relationship type where a {@link SpatioTemporalExtent} may be aggregated into one or more
     * others.
     * <p>
     * Note: This has the same meaning as, but different representation to, the
     * {@link uk.gov.gchq.magmacore.hqdm.model.Aggregation} entity type.
     * </p>
     *
     * @param spatioTemporalExtent The SpatioTemporalExtent.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder aggregated_Into(final SpatioTemporalExtent spatioTemporalExtent) {
        transferOfOwnership.addValue(AGGREGATED_INTO, new IRI(spatioTemporalExtent.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link SpatioTemporalExtent} has exactly one {@link Event} that is its beginning.
     *
     * @param event The Event.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder beginning(final Event event) {
        transferOfOwnership.addValue(BEGINNING, new IRI(event.getId()));
        return this;
    }

    /**
     * A relationship type where each {@link Activity} is the cause of one or more {@link Event}.
     *
     * @param event The Event.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder causes_M(final Event event) {
        transferOfOwnership.addValue(CAUSES, new IRI(event.getId()));
        return this;
    }

    /**
     * A causes relationship type where a {@link TransferOfOwnership}
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CAUSES} exactly one
     * {@link BeginningOfOwnership}.
     *
     * @param beginningOfOwnership The BeginningOfOwnership.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder causes_Beginning_M(final BeginningOfOwnership beginningOfOwnership) {
        transferOfOwnership.addValue(CAUSES_BEGINNING, new IRI(beginningOfOwnership.getId()));
        return this;
    }

    /**
     * A causes relationship type where a {@link TransferOfOwnership} causes exactly one
     * {@link EndingOfOwnership}.
     *
     * @param endingOfOwnership The EndingOfOwnership.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder causes_Ending_M(final EndingOfOwnership endingOfOwnership) {
        transferOfOwnership.addValue(CAUSES_ENDING, new IRI(endingOfOwnership.getId()));
        return this;
    }

    /**
     * A relationship type where a {@link SpatioTemporalExtent} may consist of one or more others.
     *
     * <p>
     * Note: This is the inverse of {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART__OF}.
     * </p>
     *
     * @param spatioTemporalExtent The SpatioTemporalExtent.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder consists__Of(final SpatioTemporalExtent spatioTemporalExtent) {
        transferOfOwnership.addValue(CONSISTS__OF, new IRI(spatioTemporalExtent.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF} relationship type where an
     * {@link Activity} may {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF} one or more
     * other {@link Activity}.
     *
     * @param activity The Activity.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder consists_Of(final Activity activity) {
        transferOfOwnership.addValue(CONSISTS_OF, new IRI(activity.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF_PARTICIPANT} relationship type
     * where a {@link TransferOfOwnership}
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF_PARTICIPANT} exactly one
     * {@link Transferor}.
     *
     * @param transferor The Transferor.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder consists_Of_Participant(final Transferor transferor) {
        transferOfOwnership.addValue(CONSISTS_OF_PARTICIPANT, new IRI(transferor.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF_PARTICIPANT} relationship type
     * where a {@link TransferOfOwnership}
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#CONSISTS_OF_PARTICIPANT} exactly one
     * {@link Transferee}.
     *
     * @param transferee The Transferee.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder consists_Of_Participant_(final Transferee transferee) {
        transferOfOwnership.addValue(CONSISTS_OF_PARTICIPANT_, new IRI(transferee.getId()));
        return this;
    }

    /**
     * A relationship type where an {@link Activity} may determine one or more {@link Thing} to be the
     * case.
     *
     * @param thing The Thing.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder determines(final Thing thing) {
        transferOfOwnership.addValue(DETERMINES, new IRI(thing.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link SpatioTemporalExtent} has exactly one {@link Event} that is its ending.
     *
     * @param event The Event.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder ending(final Event event) {
        transferOfOwnership.addValue(ENDING, new IRI(event.getId()));
        return this;
    }

    /**
     * A relationship type where a {@link Thing} may be a member of one or more {@link Class}.
     *
     * @param clazz The Class.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder member__Of(final Class clazz) {
        transferOfOwnership.addValue(MEMBER__OF, new IRI(clazz.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#MEMBER_OF} relationship type where a
     * {@link uk.gov.gchq.magmacore.hqdm.model.SociallyConstructedActivity} may be a
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#MEMBER_OF} one or more
     * {@link ClassOfSociallyConstructedActivity}.
     *
     * @param classOfSociallyConstructedActivity The ClassOfSociallyConstructedActivity.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder member_Of(
            final ClassOfSociallyConstructedActivity classOfSociallyConstructedActivity) {
        transferOfOwnership.addValue(MEMBER_OF,
                new IRI(classOfSociallyConstructedActivity.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#MEMBER_OF_KIND} relationship type where each
     * {@link Activity} is a {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#MEMBER_OF} one or more
     * {@link KindOfActivity}.
     *
     * @param kindOfActivity The KindOfActivity.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder member_Of_Kind_M(final KindOfActivity kindOfActivity) {
        transferOfOwnership.addValue(MEMBER_OF_KIND, new IRI(kindOfActivity.getId()));
        return this;
    }

    /**
     * An {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#AGGREGATED_INTO} relationship type where a
     * {@link SpatioTemporalExtent} may be part of another and the whole has emergent properties and is
     * more than just the sum of its parts.
     *
     * @param spatioTemporalExtent The SpatioTemporalExtent.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder part__Of(final SpatioTemporalExtent spatioTemporalExtent) {
        transferOfOwnership.addValue(PART__OF, new IRI(spatioTemporalExtent.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link TransferOfOwnership} may be {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF}
     * not more than one {@link ExchangeOfGoodsAndMoney}.
     *
     * @param exchangeOfGoodsAndMoney The ExchangeOfGoodsAndMoney.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder part_Of(final ExchangeOfGoodsAndMoney exchangeOfGoodsAndMoney) {
        transferOfOwnership.addValue(PART_OF, new IRI(exchangeOfGoodsAndMoney.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link uk.gov.gchq.magmacore.hqdm.model.SociallyConstructedObject} may be a
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} one or more
     * {@link AgreementExecution}.
     *
     * @param agreementExecution The AgreementExecution.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder part_Of_(final AgreementExecution agreementExecution) {
        transferOfOwnership.addValue(PART_OF_, new IRI(agreementExecution.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link SpatioTemporalExtent} may be {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF}
     * one or more {@link PossibleWorld}.
     *
     * <p>
     * Note: The relationship is optional because a {@link PossibleWorld} is not
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} any other
     * {@link SpatioTemporalExtent}.
     * </p>
     *
     * @param possibleWorld The PossibleWorld.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder part_Of_Possible_World_M(final PossibleWorld possibleWorld) {
        transferOfOwnership.addValue(PART_OF_POSSIBLE_WORLD, new IRI(possibleWorld.getId()));
        return this;
    }

    /**
     * A references relationship type where a {@link TransferOfOwnership} references exactly one
     * {@link Asset}.
     *
     * @param asset The Asset.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder references_M(final Asset asset) {
        transferOfOwnership.addValue(REFERENCES, new IRI(asset.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#PART_OF} relationship type where a
     * {@link SpatioTemporalExtent} may be a temporal part of one or more other
     * {@link SpatioTemporalExtent}.
     *
     * @param spatioTemporalExtent The SpatioTemporalExtent.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder temporal__Part_Of(final SpatioTemporalExtent spatioTemporalExtent) {
        transferOfOwnership.addValue(TEMPORAL__PART_OF, new IRI(spatioTemporalExtent.getId()));
        return this;
    }

    /**
     * A {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#TEMPORAL_PART_OF} relationship type where a
     * {@link uk.gov.gchq.magmacore.hqdm.model.State} may be a
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#TEMPORAL_PART_OF} one or more
     * {@link Individual}.
     *
     * <p>
     * Note: The relationship is optional because an {@link Individual} is not necessarily a
     * {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#TEMPORAL_PART_OF} another {@link Individual},
     * yet is a {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#MEMBER_OF}
     * {@link uk.gov.gchq.magmacore.hqdm.model.State} as well as {@link Individual}. This applies to all
     * subtypes of {@link uk.gov.gchq.magmacore.hqdm.rdf.iri.HQDM#TEMPORAL_PART_OF} that are between
     * a {@code state_of_X} and {@code X}.
     * </p>
     *
     * @param individual The Individual.
     * @return This builder.
     */
    public final TransferOfOwnershipBuilder temporal_Part_Of(final Individual individual) {
        transferOfOwnership.addValue(TEMPORAL_PART_OF, new IRI(individual.getId()));
        return this;
    }

    /**
     * Returns an instance of TransferOfOwnership created from the properties set on this builder.
     *
     * @return The built TransferOfOwnership.
     * @throws HqdmException If the TransferOfOwnership is missing any mandatory properties.
     */
    public TransferOfOwnership build() throws HqdmException {
        if (transferOfOwnership.hasValue(AGGREGATED_INTO)
                && transferOfOwnership.value(AGGREGATED_INTO).isEmpty()) {
            throw new HqdmException("Property Not Set: aggregated_into");
        }
        if (transferOfOwnership.hasValue(BEGINNING)
                && transferOfOwnership.value(BEGINNING).isEmpty()) {
            throw new HqdmException("Property Not Set: beginning");
        }
        if (!transferOfOwnership.hasValue(CAUSES)) {
            throw new HqdmException("Property Not Set: causes");
        }
        if (!transferOfOwnership.hasValue(CAUSES_BEGINNING)) {
            throw new HqdmException("Property Not Set: causes_beginning");
        }
        if (!transferOfOwnership.hasValue(CAUSES_ENDING)) {
            throw new HqdmException("Property Not Set: causes_ending");
        }
        if (transferOfOwnership.hasValue(DETERMINES)
                && transferOfOwnership.value(DETERMINES).isEmpty()) {
            throw new HqdmException("Property Not Set: determines");
        }
        if (transferOfOwnership.hasValue(ENDING)
                && transferOfOwnership.value(ENDING).isEmpty()) {
            throw new HqdmException("Property Not Set: ending");
        }
        if (transferOfOwnership.hasValue(MEMBER__OF)
                && transferOfOwnership.value(MEMBER__OF).isEmpty()) {
            throw new HqdmException("Property Not Set: member__of");
        }
        if (transferOfOwnership.hasValue(MEMBER_OF)
                && transferOfOwnership.value(MEMBER_OF).isEmpty()) {
            throw new HqdmException("Property Not Set: member_of");
        }
        if (!transferOfOwnership.hasValue(MEMBER_OF_KIND)) {
            throw new HqdmException("Property Not Set: member_of_kind");
        }
        if (transferOfOwnership.hasValue(PART__OF)
                && transferOfOwnership.value(PART__OF).isEmpty()) {
            throw new HqdmException("Property Not Set: part__of");
        }
        if (transferOfOwnership.hasValue(PART_OF)
                && transferOfOwnership.value(PART_OF).isEmpty()) {
            throw new HqdmException("Property Not Set: part_of");
        }
        if (transferOfOwnership.hasValue(PART_OF_)
                && transferOfOwnership.value(PART_OF_).isEmpty()) {
            throw new HqdmException("Property Not Set: part_of_");
        }
        if (!transferOfOwnership.hasValue(PART_OF_POSSIBLE_WORLD)) {
            throw new HqdmException("Property Not Set: part_of_possible_world");
        }
        if (!transferOfOwnership.hasValue(REFERENCES)) {
            throw new HqdmException("Property Not Set: references");
        }
        if (transferOfOwnership.hasValue(TEMPORAL__PART_OF)
                && transferOfOwnership.value(TEMPORAL__PART_OF).isEmpty()) {
            throw new HqdmException("Property Not Set: temporal__part_of");
        }
        if (transferOfOwnership.hasValue(TEMPORAL_PART_OF)
                && transferOfOwnership.value(TEMPORAL_PART_OF).isEmpty()) {
            throw new HqdmException("Property Not Set: temporal_part_of");
        }
        return transferOfOwnership;
    }
}
