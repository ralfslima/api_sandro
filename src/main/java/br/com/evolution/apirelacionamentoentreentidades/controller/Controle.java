package br.com.evolution.apirelacionamentoentreentidades.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.evolution.apirelacionamentoentreentidades.model.Marca;
import br.com.evolution.apirelacionamentoentreentidades.model.Produto;
import br.com.evolution.apirelacionamentoentreentidades.model.VinculacaoRequest;
import br.com.evolution.apirelacionamentoentreentidades.repository.MarcaRepositorio;
import br.com.evolution.apirelacionamentoentreentidades.repository.ProdutoRespositorio;

@RestController
public class Controle {

    @Autowired
    private MarcaRepositorio marcaRepositorio;

    @Autowired
    private ProdutoRespositorio produtoRepositorio;

    // Método para cadastrar uma Marca
    @PostMapping("/cadastrarMarca")
    public Marca cadastrarMarca(@RequestBody Marca marca){
        return marcaRepositorio.save(marca);
    }

    // Método para cadastrar um produto
    @PostMapping("/cadastrarProduto")
    public Produto cadastrarProduto(@RequestBody Produto produto){
        return produtoRepositorio.save(produto);
    }

    // MINHAS MODIFICAÇÕES
    @GetMapping("/listar")
    public List<Marca> listarProdutos() {
        return (List<Marca>) marcaRepositorio.findAll();
    }

    // Método para vincular uma Marca a um determinado Produto
    @PostMapping("/vincular")
    public void vincularMarcaProduto(@RequestBody VinculacaoRequest vinculacaoRequest) {
        Produto produto = produtoRepositorio.findByCodigo(vinculacaoRequest.getCodigoProduto());
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado com o código: " + vinculacaoRequest.getCodigoProduto());
        }

        Marca marca = marcaRepositorio.findByCodigo(vinculacaoRequest.getCodigoMarca());
        if (marca == null) {
            throw new RuntimeException("Marca não encontrada com o código: " + vinculacaoRequest.getCodigoMarca());
        }

        // Adicionar a Marca à lista de marcas do Produto
        if (!produto.getMarcas().contains(marca)) {
            produto.getMarcas().add(marca);
        }

        // Adicionar o Produto à lista de produtos da Marca
        if (!marca.getProdutos().contains(produto)) {
            marca.getProdutos().add(produto);
        }

        // Salvar as mudanças
        produtoRepositorio.save(produto);
        marcaRepositorio.save(marca);
    }
}
