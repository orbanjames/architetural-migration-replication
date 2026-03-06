package com.ecomapp.services.synod;


import com.ecomapp.models.SynodSection;
import com.ecomapp.services.GenericService;
import reactor.core.publisher.Flux;

public interface SectionService extends GenericService<SynodSection> {

    Flux<SynodSection> getSectionsForSynod(int synodId);

    Flux<SynodSection> getSectionsForCategory(int categoryId);

    Flux<SynodSection> getSectionsForRegistrar(int registrarId);
}