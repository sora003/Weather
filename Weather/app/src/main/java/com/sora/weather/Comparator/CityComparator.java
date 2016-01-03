package com.sora.weather.Comparator;

import java.util.Comparator;

import static com.sora.weather.Comparator.Cn2Spell.converterToFirstSpell;

/**
 * Created by Sora on 2016/1/2.
 */
public class CityComparator implements Comparator<String> {

    @Override
    public int compare(String lhs, String rhs) {
        String cn1 = converterToFirstSpell(lhs);
        String cn2 = converterToFirstSpell(rhs);
        return cn1.compareTo(cn2);
    }

}
