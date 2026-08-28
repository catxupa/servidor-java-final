package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.ServicoNaoEncontradoException;
import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository repositorio;

    public ServicoService(ServicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Page<ServicoModel> listarTodos(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    public ServicoModel save(ServicoModel servico) {

        return repositorio.save(servico);
    }

    public List<ServicoModel> pesquisarServicos(String titulo) {

        List<ServicoModel> resultado =
                repositorio.findByTituloContainingIgnoreCase(titulo);

        if (resultado.isEmpty()) {
            throw new ServicoNaoEncontradoException(
                    "O serviço '" + titulo + "' não existe no catálogo."
            );
        }

        return resultado;
    }


    // buscar servico por id
    public ServicoModel buscarServicoPorId(Long id) {
        return repositorio

                .findById(id)
                .orElseThrow(() -> new ServicoNaoEncontradoException(
                        "O serviço com o ID " + id + " não existe no catálogo."
                ));
    }

    public List<ServicoModel> aplicarDescontoEmAtivos(Double percentagem) {
        // nova verificacao
        if (percentagem < 0 || percentagem > 100) {
            throw new IllegalArgumentException("Desconto inválido.");
        }

        List<ServicoModel> listaAtivos = repositorio.findByestativoTrue();
        for (ServicoModel s : listaAtivos) {
            Double precoOriginal = s.getPreco(); // assume-se a existência deste getter na entidade
            Double precoComDesconto = precoOriginal * (percentagem / 100);
            double novoPreco = precoOriginal - precoComDesconto;

            s.setPrecoComDesconto(novoPreco);
        }

        return repositorio.saveAll(listaAtivos);
    }
}