/*
 * Copyright (c) 2014, Araz Abishov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.dhis2.ehealthMobile.utils.date.iterators;

import org.dhis2.ehealthMobile.utils.date.CustomDateIteratorClass;
import org.dhis2.ehealthMobile.utils.date.DateHolder;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;

public class BiMonthIterator extends CustomDateIteratorClass<ArrayList<DateHolder>> {
    private static final String B1 = "01B";
    private static final String B3 = "03B";
    private static final String B5 = "05B";
    private static final String B7 = "07B";
    private static final String B9 = "09B";
    private static final String B11 = "11B";

    private static final String DATE_LABEL_FORMAT = "%s - %s %s";

    private int openFuturePeriods;
    private LocalDate checkDate;
    private LocalDate cPeriod;

    public BiMonthIterator(int openFP) {
        openFuturePeriods = openFP;
        cPeriod = new LocalDate(currentDate.getYear(), JAN, 1);
        checkDate = new LocalDate(cPeriod);
    }

    @Override
    public ArrayList<DateHolder> current() {
        if (!hasNext()) {
            return previous();
        } else {
            return generatePeriod();
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext(checkDate);
    }

    private boolean hasNext(LocalDate date) {
        if (openFuturePeriods > 0) {
            return true;
        } else {
            return currentDate.isAfter(date.plusMonths(2));
        }
    }

    @Override
    public ArrayList<DateHolder> next() {
        cPeriod = cPeriod.plusYears(1);
        return generatePeriod();
    }

    @Override
    public ArrayList<DateHolder> previous() {
        cPeriod = cPeriod.minusYears(1);
        return generatePeriod();
    }

    @Override
    protected ArrayList<DateHolder> generatePeriod() {
        int counter = 0;
        ArrayList<DateHolder> dates = new ArrayList<DateHolder>();
        checkDate = new LocalDate(cPeriod);

        while (hasNext(checkDate) && counter < 6) {
            int cMonth = checkDate.getMonthOfYear();
            String year = checkDate.year().getAsString();

            String date;
            String label;

            if (cMonth < FEB) {
                date = year + B1;
                label = String.format(DATE_LABEL_FORMAT, JAN_STR, FEB_STR, year);
            } else if ((cMonth >= FEB) && (cMonth < APR)) {
                date = year + B3;
                label = String.format(DATE_LABEL_FORMAT, MAR_STR, APR_STR, year);
            } else if ((cMonth >= APR) && (cMonth < JUN)) {
                date = year + B5;
                label = String.format(DATE_LABEL_FORMAT, MAY_STR, JUN_STR, year);
            } else if ((cMonth >= JUN) && (cMonth < AUG)) {
                date = year + B7;
                label = String.format(DATE_LABEL_FORMAT, JUL_STR, AUG_STR, year);
            } else if ((cMonth >= AUG) && (cMonth < OCT)) {
                date = year + B9;
                label = String.format(DATE_LABEL_FORMAT, SEP_STR, OCT_STR, year);
            } else {
                date = year + B11;
                label = String.format(DATE_LABEL_FORMAT, NOV_STR, DEC_STR, year);
            }

            DateHolder dateHolder = new DateHolder(date, label);
            dates.add(dateHolder);

            counter++;
            checkDate = checkDate.plusMonths(2);
        }

        Collections.reverse(dates);
        return dates;
    }

}
