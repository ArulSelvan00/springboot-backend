package com.example.demo;

import com.example.demo.Offer;
import com.example.demo.OfferRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfferController {

    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @GetMapping
    public List<Offer> getOffers() {
        return offerRepository.findAll();
    }
}
