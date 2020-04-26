package org.digitalmind.buildingblocks.core.jpaauditor.entity.enumeration;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public enum SignMethod {

    UPLOAD(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.POST).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.PAPER).build()
            ),
            "Sign using a scanned file containing party signature"),
    UPLOAD_METADATA(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.POST).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.PAPER).build()
            ),
            "Sign using a scanned file containing all documents to be signed enriched with metadata in qr"),
    DRAW(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.CUSTOMER).signFlow(SignFlow.DIGITAL).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.CUSTOMER).signFlow(SignFlow.PAPER).build()
            ),
            "Sign using handwritten signature"),
    OTC(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.SMS).partyType(PartyType.CUSTOMER).signFlow(SignFlow.DIGITAL).build(),
                    SignEligibility.builder().signChannel(SignChannel.SMS).partyType(PartyType.CUSTOMER).signFlow(SignFlow.PAPER).build(),
                    SignEligibility.builder().signChannel(SignChannel.SMS).partyType(PartyType.CUSTOMER).signFlow(SignFlow.POST).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.DIGITAL).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.PAPER).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.POST).build()
            ),
            "Sign by entering an OTC code"),
    CONFIRMATION(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.CUSTOMER).signFlow(SignFlow.DIGITAL).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.CUSTOMER).signFlow(SignFlow.POST).build()
            ),
            "Sign without any additional confirmation"),
    REVOKE(
            Arrays.asList(
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.DIGITAL).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.PAPER).build(),
                    SignEligibility.builder().signChannel(SignChannel.UI).partyType(PartyType.AGENT).signFlow(SignFlow.POST).build()
            ),
            "Revoke a signed process")
    ;

    private String description;
    private List<SignEligibility> eligibilities;

    SignMethod(List<SignEligibility> eligibilities, String description) {
        this.description = description;
        this.eligibilities = eligibilities;
    }

    public static List<SignMethod> getEligibleSignMethods(SignEligibility signEligibility) {
        return Arrays.asList(SignMethod.values()).stream().filter(signMethod -> signMethod.isEligible(signEligibility)).collect(Collectors.toList());
    }

    private boolean isEligible(SignEligibility eligibility) {
        return this.eligibilities.contains(eligibility);
    }

    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @EqualsAndHashCode
    @ToString
    public static class SignEligibility {
        private SignChannel signChannel;
        private PartyType partyType;
        private SignFlow signFlow;
    }


}
