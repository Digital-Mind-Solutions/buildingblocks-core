package org.digitalmind.buildingblocks.core.phoneutils.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.digitalmind.buildingblocks.core.phoneutils.exception.PhoneUtilException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class PhoneUtilService {

    private static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private static PhoneNumberToCarrierMapper phoneToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static String DEFAULT_REGION = "RO";

    public Phonenumber.PhoneNumber parse(String phoneNumber) throws PhoneUtilException {
        return parse(phoneNumber, DEFAULT_REGION);
    }

    public Phonenumber.PhoneNumber parse(String phoneNumber, String region) throws PhoneUtilException {
        try {
            return phoneUtil.parse(phoneNumber, DEFAULT_REGION);
        } catch (NumberParseException e) {
            throw new PhoneUtilException(e);
        }
    }

    public boolean isValid(String phoneNumber) {
        return isValid(phoneNumber, DEFAULT_REGION);
    }

    public boolean isValid(String msisdn, String region) {
        try {
            Phonenumber.PhoneNumber phoneNumber = parse(msisdn, DEFAULT_REGION);
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (PhoneUtilException e) {
            return false;
        }
    }

    public String formatInternational(Phonenumber.PhoneNumber phoneNumber) {
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    public String formatNational(Phonenumber.PhoneNumber phoneNumber, boolean excludeLeadingZero) {
        String nationalPhoneNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        if (excludeLeadingZero && nationalPhoneNumber.startsWith("0")) {
            nationalPhoneNumber = nationalPhoneNumber.substring(1).replace(" ", "");
        }
        return nationalPhoneNumber;
    }

    public boolean isNational(Phonenumber.PhoneNumber phoneNumber) {
        return phoneUtil.getRegionCodeForNumber(phoneNumber).equalsIgnoreCase(DEFAULT_REGION);
    }

    public String getCountryCode(Phonenumber.PhoneNumber phoneNumber) {
        return phoneUtil.getRegionCodeForNumber(phoneNumber);
    }

    public String getNumberType(Phonenumber.PhoneNumber phoneNumber) {
        return phoneUtil.getNumberType(phoneNumber).name();
    }

    public String getCarrier(Phonenumber.PhoneNumber phoneNumber, Locale locale) {
        return phoneToCarrierMapper.getNameForNumber(phoneNumber, locale);
    }

}
