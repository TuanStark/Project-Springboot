package com.stark.webbanhang.helper.base.construct;


import com.stark.webbanhang.helper.base.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public abstract class RestfullController<P, S, U> {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public abstract ResponseObject<?> getPaginate(@RequestParam P p);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public abstract ResponseObject<?> get(@PathVariable UUID id); // if dto required check null

    @PostMapping
    @ResponseStatus(HttpStatus.OK) // CREATED
    public abstract ResponseObject<?> store(@Validated @RequestBody S s); // if dto required check null

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) //ACCEPTED
    public abstract ResponseObject<?> update(@PathVariable UUID id, @Validated @RequestBody U u); // if dto required check null

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) //ACCEPTED
    public abstract ResponseObject<?> destroy(@PathVariable UUID id); // if dto required check null
}
