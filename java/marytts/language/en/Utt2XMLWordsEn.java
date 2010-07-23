/**
 * Copyright 2000-2006 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * This file is part of MARY TTS.
 *
 * MARY TTS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package marytts.language.en;

import java.util.Locale;

import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.language.en_US.datatypes.USEnglishDataTypes;
import marytts.modules.Utt2XMLBase;
import marytts.modules.XML2UttBase;

import org.w3c.dom.Element;

import com.sun.speech.freetts.FeatureSet;
import com.sun.speech.freetts.Item;
import com.sun.speech.freetts.Relation;
import com.sun.speech.freetts.Utterance;



/**
 * Convert FreeTTS utterances into MaryXML format
 * (Words, English).
 *
 * @author Marc Schr&ouml;der
 */

public class Utt2XMLWordsEn extends Utt2XMLBase
{
    public Utt2XMLWordsEn()
    {
        super("Utt2XML WordsEn",
              USEnglishDataTypes.FREETTS_WORDS,
              MaryDataType.WORDS,
              Locale.ENGLISH);
    }

}

