package com.brfyamada.webflux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Teste {

    public Mono<List<String>> test() {

        List<String> list1 = Arrays.asList("Au", "AO", "Ai");
        List<String> list2 = Arrays.asList("Bu", "BO", "Bi");
        List<String> list3 = Arrays.asList("Bu", "BO", "Bi");
        List<String> list4 = Arrays.asList("Bu", "BO", "Bi");
        List<String> list5 = Arrays.asList("Bu", "BO", "Bi");

        Flux<List<String>> flux = Flux.just(list1,list2,list3,list4,list5);

        Flux<String> flux2 = flux.map(it -> it)
                .flatMapIterable(it ->it)
                .map(it -> it);

        return flux2.collectList().map(it -> it);


    }

    public Mono<Sindicato> test2() {
        return processa()
                .flatMap(this::salvar);
    }

    public Mono<Sindicato> salvar(Sindicato sindicato){
        return Mono.just(sindicato)
                .flatMap(it -> {
                    System.out.println("Salvando no Banco");
                    return Mono.just(it);
                });
    }

    public Mono<Sindicato> processa() {

        Empresa emp1 = new Empresa();
        Empresa emp2 = new Empresa();
        Empresa emp3 = new Empresa();

        Pessoa p1 = new Pessoa("Carlos");
        Pessoa p2 = new Pessoa("João");
        Pessoa p3 = new Pessoa("Paulo");
        Pessoa p4 = new Pessoa("Marcoso");
        Pessoa p5 = new Pessoa("Sergio");
        Pessoa p6 = new Pessoa("Santos");
        Pessoa p7 = new Pessoa("Marcelo");

        emp1.funcionarios.addAll(Arrays.asList(p1,p2));
        emp2.funcionarios.addAll(Arrays.asList(p3,p4));
        emp3.funcionarios.addAll(Arrays.asList(p5,p6,p7));

        Sindicato sind = new Sindicato();
        sind.getEmpresas().addAll(Arrays.asList(emp1,emp2,emp3));

        List<Empresa> empresas = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
/*
        return Flux.just(sind)
                .map(it -> it.getEmpresas())
                .flatMapIterable(it -> it)
                .flatMap(it -> {
                    Mono<Boolean> bol = addPaticipante(it.getFuncionarios(),sind);
                    bol.subscribe();
                    return Mono.just(it);
                }).map(it -> it).then().thenReturn(sind);



        return Flux.just(sind)
                .map(it -> it.getEmpresas())
                .flatMapIterable(it -> it)
                .map(it -> it.getFuncionarios())
                .flatMapIterable(it -> it)
                .flatMap(it -> {
                    Mono<Boolean> bol = addPaticipante(it,sind);
                    bol.subscribe();
                    return Mono.just(it);
                }).map(it -> it).then().thenReturn(sind);

 */
        return Flux.just(sind)
                .map(it -> it.getEmpresas())
                .flatMapIterable(it -> it)
                .map(it -> it.getFuncionarios())
                .flatMapIterable(it -> it)
                .flatMap(it -> addPaticipante(it,sind).thenReturn(it))
                .map(it -> it)
                .then().thenReturn(sind);
    }

    public Mono<Boolean> addPaticipante(Pessoa pessoa, Sindicato sindicato){
        sindicato.getParticipantes().add(pessoa);
        return Mono.just(true);
    }
/*

    public Mono<Boolean> addPaticipante(List<Pessoa> pessoas, Sindicato sindicato){
        sindicato.getParticipantes().addAll(pessoas);
        return Mono.just(true);
    }
*/

    public class Empresa implements Serializable {
        public List<Pessoa> funcionarios = new ArrayList<>();

        public List<Pessoa> getFuncionarios() {
            return funcionarios;
        }

        public void setFuncionarios(List<Pessoa> funcionarios) {
            this.funcionarios = funcionarios;
        }
    }

    public class Pessoa implements Serializable{

        private String nome;

        public Pessoa(String nome){
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public class Sindicato implements Serializable{
        public List<Pessoa> participantes = new ArrayList<>();

        public List<Empresa> empresas = new ArrayList<>();

        public List<Pessoa> getParticipantes() {
            return participantes;
        }

        public void setParticipantes(List<Pessoa> participantes) {
            this.participantes = participantes;
        }

        public List<Empresa> getEmpresas() {
            return empresas;
        }

        public void setEmpresas(List<Empresa> empresas) {
            this.empresas = empresas;
        }
    }
}

