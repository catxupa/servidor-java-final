package com.labanta.servidorlocal.controller;
import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.labanta.servidorlocal.service.ServicoService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {
    
    private final ServicoService servicoService;
    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;


    // Injeção do Serviço
    public ServicoController(ServicoService servicoService, ExchangeService exchangeService, EmailService emailService, FileStorageService fileStorageService ) {
        this.servicoService = servicoService;
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        
    }
    
    //getAll services
    @Operation(
        summary = "Listar todos os serviços", 
        description = "Retorna uma lista com todos os serviços")

    @GetMapping
    public Page<ServicoModel> listarTodos(
        @PageableDefault(
                page = 0,
                size = 5,
                sort = "id",
                direction = Sort.Direction.DESC)

        Pageable pageable) {

        return servicoService.listarTodos(pageable);
    }

    //get service by id
    @Operation(
        summary = "Buscar serviço por ID",
        description = "Retorna o serviço com o ID pesquisado")
         
    @GetMapping("/{id}")
    public ServicoModel buscarServicoPorId(
            @PathVariable Long id) {
        return servicoService.buscarServicoPorId(id);
    }

    //create service
    @Operation(
        summary = "Criar um novo serviço",
        description = "Cria um novo serviço")
         
    @SecurityRequirement(
        name = "bearerAuth")

    @PostMapping
    public ServicoModel criarServico(
            @RequestBody ServicoModel novoServico) {
        return servicoService.save(novoServico);
    }

    //get discount services
    @Operation(
        summary = "Listar serviços com desconto",
        description = "Retorna uma lista com os serviços com desconto")
         
    @GetMapping("/desconto-aplicado")
    public List<ServicoResponseDTO> aplicarDesconto(
            @RequestParam Double percentagem) {

        // 1. Pedir a aplicacao do desconto à camada Service
      List<ServicoModel> servicosComDesconto =
              servicoService.aplicarDescontoEmAtivos(percentagem);

        // 2. Converter a lista de Entidades para Lista de DTOs (Filtro)
        List<ServicoResponseDTO> respostaDTO = new ArrayList<>();
        for (ServicoModel s : servicosComDesconto) {

            // Utiliza o precoComDesconto se existir, senão usa o preco original
            Double precoExibir = s.getPrecoComDesconto() != null ? s.getPrecoComDesconto() : s.getPreco();
            respostaDTO.add(new ServicoResponseDTO(s.getTitulo(), precoExibir));
        }

        // 3. Devolver lista filtrada
        return respostaDTO;
    }

   
    //get services by title
    @Operation(
        summary = "Pesquisar serviços por título",
        description = "Retorna uma lista com os serviços que contêm o título pesquisado")
         
    @GetMapping("/pesquisa")
    public ResponseEntity<List<ServicoModel>> pesquisarServicos(
            @RequestParam String titulo) {

        List<ServicoModel> resultado =
                servicoService.pesquisarServicos(titulo);

        return ResponseEntity.ok(resultado);
    }


    //post orcamento
    @Operation(
        summary = "Pedir orçamento",
        description = "Envia um orçamento para o email do cliente")

    @SecurityRequirement(
        name = "bearerAuth")
        
    @PostMapping("/{id}/orcamento")
    public String pedirOrcamento(
        @PathVariable Long id,
        @RequestParam String emailDestino,
        @RequestParam(defaultValue = "CVE") String moeda) {

        // 1. Ir à Base de Dados buscar o Serviço
        ServicoModel servico = servicoService.buscarServicoPorId(id);

        // 2. converter o preço
        Double precoConvertido = exchangeService.converterPreco(
                servico.getPreco(), moeda);


        // 3. Enviar o resultado para o Gmail do cliente
        emailService.enviarEmailOrcamento(
                emailDestino,
                servico.getTitulo(),
                precoConvertido,
                moeda
        );

        return "Orçamento calculado e enviado com sucesso para " + emailDestino + "!";
    }


    @Operation(
            summary = "Carregar capa de servico",
            description = "Rota para upload da capa do servico com id"
    )
    @SecurityRequirement(
            name = "bearerAuth")

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String>uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long id
    ){
        ServicoModel servico = servicoService.buscarServicoPorId(id);
        String fileUploaded = fileStorageService.StoreImages(file);

        servico.setImageK(fileUploaded);
        servicoService.save(servico);

        return  ResponseEntity.ok("FICHEIRO CARREGADO COM SUCESSO: " + fileUploaded);
    }
}







