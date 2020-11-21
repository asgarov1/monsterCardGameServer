package at.fhtw.bif3.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PackageControllerTest {

    @Test
    public void extractCardsShouldWork(){
        String contentString = "[{\"Id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, " +
                                "{\"Id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"Name\":\"RegularSpell\", \"Weakness\": 45.0, \"Damage\": 50.0}, " +
                                "{\"Id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"Name\":\"Knight\", \"Damage\": 20.0}, " +
                                "{\"Id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"Name\":\"RegularSpell\", \"Weakness\": 45.0, \"Damage\": 45.0}, " +
                                "{\"Id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"Name\":\"FireElf\", \"Damage\": 25.0}]";

        var cards = new PackagesController().extractCards(contentString);
        assertEquals(5, cards.size());
        cards.forEach(card -> assertNotNull(card.getId()));
        cards.forEach(card -> assertNotNull(card.getName()));
    }
}