package com.mediscreen.webapp.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {

    @Test
    public void nettoyerTags(){
        String note = "<p>J'ajoute une note :</p><ol><li>Traitement : médicament</li><li>Préconisation : faire du sport</li></ol>";
        assertThat(StringUtil.nettoyerTags(note)).isEqualTo("J'ajoute une note :Traitement : médicamentPréconisation : faire du sport");
        note = "<p><br></p><ol><li><br></li><li><br></li></ol>";
        assertThat(StringUtil.nettoyerTags(note)).isEqualTo("");
    }

}
