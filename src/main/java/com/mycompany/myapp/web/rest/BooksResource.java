package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.security.CommonUtils;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.criteria.BooksCriteria;
import com.mycompany.myapp.service.dto.AvatarAttributesDTO;
import com.mycompany.myapp.service.dto.BooksPageDTO;
import com.mycompany.myapp.service.dto.ImageCreatorDTO;
import com.mycompany.myapp.service.dto.ImageParameterDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Books}.
 */
@RestController
@RequestMapping("/api")
public class BooksResource {

    private final Logger log = LoggerFactory.getLogger(BooksResource.class);

    private static final String ENTITY_NAME = "books";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksService booksService;

    private final BooksRepository booksRepository;

    private final BooksQueryService booksQueryService;

    private final PDFGenarator pdfGenarator;

    private final PageLayersDetailsService pageLayersDetailsService;
    private final PageLayersService pageLayersService;
    private final BooksPageService booksPageService;

    private final AvatarCharactorService avatarCharactorService;
    private final StylesDetailsService stylesDetailsService;
    private final StylesService stylesService;
    private final OptionsService optionsService;
    private final AvatarAttributesService avatarAttributesService;

    private final LayerGroupService layerGroupService;
    private final BucketController bucketController;
    private final ImagesService imagesService;

    public BooksResource(
        BooksService booksService,
        BooksRepository booksRepository,
        BooksQueryService booksQueryService,
        PDFGenarator pdfGenarator,
        PageLayersDetailsService pageLayersDetailsService,
        PageLayersService pageLayersService,
        BooksPageService booksPageService,
        AvatarCharactorService avatarCharactorService,
        StylesDetailsService stylesDetailsService,
        StylesService stylesService,
        OptionsService optionsService,
        AvatarAttributesService avatarAttributesService,
        LayerGroupService layerGroupService,
        BucketController bucketController,
        ImagesService imagesService
    ) {
        this.booksService = booksService;
        this.booksRepository = booksRepository;
        this.booksQueryService = booksQueryService;
        this.pdfGenarator = pdfGenarator;
        this.pageLayersDetailsService = pageLayersDetailsService;
        this.pageLayersService = pageLayersService;
        this.booksPageService = booksPageService;
        this.avatarCharactorService = avatarCharactorService;
        this.stylesDetailsService = stylesDetailsService;
        this.stylesService = stylesService;
        this.optionsService = optionsService;
        this.avatarAttributesService = avatarAttributesService;
        this.layerGroupService = layerGroupService;
        this.bucketController = bucketController;
        this.imagesService = imagesService;
    }

    /**
     * {@code POST  /books} : Create a new books.
     *
     * @param books the books to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new books, or with status {@code 400 (Bad Request)} if the books has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books")
    public ResponseEntity<Books> createBooks(@Valid @RequestBody Books books) throws URISyntaxException {
        log.debug("REST request to save Books : {}", books);
        if (books.getId() != null) {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Books result = booksService.save(books);
        return ResponseEntity
            .created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books/:id} : Updates an existing books.
     *
     * @param id the id of the books to save.
     * @param books the books to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated books,
     * or with status {@code 400 (Bad Request)} if the books is not valid,
     * or with status {@code 500 (Internal Server Error)} if the books couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<Books> updateBooks(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Books books)
        throws URISyntaxException {
        log.debug("REST request to update Books : {}, {}", id, books);
        if (books.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, books.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Books result = booksService.update(books);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, books.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books/:id} : Partial updates given fields of an existing books, field will ignore if it is null
     *
     * @param id the id of the books to save.
     * @param books the books to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated books,
     * or with status {@code 400 (Bad Request)} if the books is not valid,
     * or with status {@code 404 (Not Found)} if the books is not found,
     * or with status {@code 500 (Internal Server Error)} if the books couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Books> partialUpdateBooks(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Books books
    ) throws URISyntaxException {
        log.debug("REST request to partial update Books partially : {}, {}", id, books);
        if (books.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, books.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Books> result = booksService.partialUpdate(books);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, books.getId().toString())
        );
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Books>> getAllBooks(
        BooksCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Books by criteria: {}", criteria);
        Page<Books> page = booksQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books/count} : count all the books.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books/count")
    public ResponseEntity<Long> countBooks(BooksCriteria criteria) {
        log.debug("REST request to count Books by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books/:id} : get the "id" books.
     *
     * @param id the id of the books to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the books, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Books> getBooks(@PathVariable Long id) {
        log.debug("REST request to get Books : {}", id);
        Optional<Books> books = booksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(books);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" books.
     *
     * @param id the id of the books to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBooks(@PathVariable Long id) {
        log.debug("REST request to delete Books : {}", id);
        booksService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/printReceipts")
    public byte[] receipt(String booksCode, String storeCode) throws IOException, JRException {
        Optional<Books> book = booksService.findOneByCode(booksCode);
        if (!book.isPresent()) {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Books books = book.get();

        byte[] receipt = pdfGenarator.pdfCreator(books, "ADMIN");

        //        Set<ImageParameterDTO> imageParameterDTOSet = new HashSet<>();
        //
        //        ImageParameterDTO i

        //        mageParameterDTO = new ImageParameterDTO();
        //
        //        imageParameterDTO.setImageUrl("https://alphadevs-logos.s3.ap-south-1.amazonaws.com/abc.png");
        //        imageParameterDTO.setX(68);
        //        imageParameterDTO.setY(120);
        //        imageParameterDTO.setHeight(398);
        //        imageParameterDTO.setWidth(446);
        //
        //        imageParameterDTOSet.add(imageParameterDTO);
        //
        //        ImageParameterDTO imageParameterDTO1 = new ImageParameterDTO();
        //
        //        imageParameterDTO1.setImageUrl("https://wikunum-lite-generic.s3.ap-south-1.amazonaws.com/ADPanicBuying1670238944.png");
        //        imageParameterDTO1.setX(34);
        //        imageParameterDTO1.setY(68);
        //        imageParameterDTO1.setHeight(421);
        //        imageParameterDTO1.setWidth(330);
        //        imageParameterDTOSet.add(imageParameterDTO1);
        //
        //        ImageCreatorDTO obj = new ImageCreatorDTO();
        //        obj.setPageHeight(595);
        //        obj.setPageWidth(595);
        //        obj.setImage(imageParameterDTOSet);
        //
        //        layerGroupService.imageCreator(obj);

        return receipt;
    }

    @PostMapping("/createNewBooks")
    public ResponseEntity<Books> createNewBooks(@Valid @RequestBody Books books) throws URISyntaxException {
        log.debug("REST request to save Books : {}", books);
        if (books.getId() != null) {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String bookCode = CommonUtils.generateCode(books.getName());
        Optional<Books> book = booksService.findOneByCode(bookCode);
        if (book.isPresent()) {
            throw new BadRequestAlertException("A new books cannot already have an CODE", ENTITY_NAME, "codeexists");
        }
        books.setCode(bookCode);
        Books result = booksService.save(books);
        return ResponseEntity
            .created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/updateBooksPages")
    public Books createNewBook(@Valid @RequestBody BooksPageDTO booksPageDTO) throws URISyntaxException {
        if (booksPageDTO.getCode() == null || booksPageDTO.getCode().isEmpty()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idexists");
        }
        Optional<Books> books = booksService.findOneByCode(booksPageDTO.getCode());
        if (!books.isPresent()) {
            throw new BadRequestAlertException("Invalid book", ENTITY_NAME, "idexists");
        }
        if (booksPageDTO.getBooksPages().size() < 1) {
            throw new BadRequestAlertException("empty pages", ENTITY_NAME, "idexists");
        }
        Set<BooksPage> booksPages = booksPageDTO.getBooksPages();
        for (BooksPage bookPage : booksPages) {
            if (bookPage.getId() == null) {
                Set<PageLayers> pageLayers = bookPage.getPageDetails();
                for (PageLayers pageLayer : pageLayers) {
                    if (pageLayer.getId() == null) {
                        Set<PageLayersDetails> pageLayersDetails = pageLayer.getPageElementDetails();
                        for (PageLayersDetails pageLayerDetail : pageLayersDetails) {
                            if (pageLayerDetail.getId() == null) {
                                pageLayersDetailsService.save(pageLayerDetail);
                            }
                        }
                        pageLayersService.save(pageLayer);
                    }
                }
                booksPageService.save(bookPage);
            }
        }
        Books book = books.get();
        book.setBooksPages(booksPages);
        return booksService.update(book);
    }

    @PutMapping("/updateBooksAvatarAttributes")
    public Books updateBooksAvatarAttributes(@Valid @RequestBody AvatarAttributesDTO avatarAttributesDTO) throws URISyntaxException {
        if (avatarAttributesDTO.getCode() == null || avatarAttributesDTO.getCode().isEmpty()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idexists");
        }
        Optional<Books> books = booksService.findOneByCode(avatarAttributesDTO.getCode());
        if (!books.isPresent()) {
            throw new BadRequestAlertException("Invalid book", ENTITY_NAME, "idexists");
        }
        if (avatarAttributesDTO.getAvatarAttributes().size() < 1) {
            throw new BadRequestAlertException("empty avatarAttributes", ENTITY_NAME, "null attributes");
        }

        for (AvatarAttributes avatarAttributes : avatarAttributesDTO.getAvatarAttributes()) {
            for (AvatarCharactor avatarCharactor : avatarAttributes.getAvatarCharactors()) {
                avatarCharactorService.save(avatarCharactor);
            }
            for (Styles styles : avatarAttributes.getStyles()) {
                stylesService.save(styles);
                for (StylesDetails stylesDetails : styles.getStylesDetails()) {
                    stylesDetailsService.save(stylesDetails);
                }
            }
            for (Options options : avatarAttributes.getOptions()) {
                optionsService.save(options);
            }
            avatarAttributesService.save(avatarAttributes);
        }
        Books book = books.get();
        book.setAvatarAttributes(avatarAttributesDTO.getAvatarAttributes());
        return booksService.update(book);
    }
    //    @PostMapping("/images")
    //    public ResponseEntity<Images> uploadImages(@Valid @RequestBody Images images) throws URISyntaxException {
    //        log.debug("REST request to upload Images : {}", images);
    //        if (images.getId() != null) {
    //            throw new BadRequestAlertException("A new images cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //        images = bucketController.uploadFileByteNonSigned(images, "PanicBuying");
    //        Images result = imagesService.save(images);
    //
    //        return ResponseEntity
    //            .created(new URI("/api/images/" + result.getId()))
    //            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
    //            .body(result);
    //    }
    //    @PutMapping("/updateBooksAvatarAttributes")
    //    public Books updateBooksAvatarAttributes(@Valid @RequestBody BooksPageDTO booksPageDTO) throws URISyntaxException {
    //        if (booksPageDTO.getCode() == null || booksPageDTO.getCode().isEmpty()) {
    //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idexists");
    //        }
    //        Optional<Books> books = booksService.findOneByCode(booksPageDTO.getCode());
    //        if (!books.isPresent()) {
    //            throw new BadRequestAlertException("Invalid book", ENTITY_NAME, "idexists");
    //        }
    //        if (booksPageDTO.getBooksPages().size() < 1) {
    //            throw new BadRequestAlertException("empty pages", ENTITY_NAME, "idexists");
    //        }
    //        Set<BooksPage> booksPages = booksPageDTO.getBooksPages();
    //        for (BooksPage bookPage : booksPages) {
    //            if (bookPage.getId() == null) {
    //                Set<PageLayers> pageLayers = bookPage.getPageDetails();
    //                for (PageLayers pageLayer : pageLayers) {
    //                    if (pageLayer.getId() == null) {
    //                        Set<PageLayersDetails> pageLayersDetails = pageLayer.getPageElementDetails();
    //                        for (PageLayersDetails pageLayerDetail : pageLayersDetails) {
    //                            if (pageLayerDetail.getId() == null) {
    //                                pageLayersDetailsService.save(pageLayerDetail);
    //                            }
    //                        }
    //                        pageLayersService.save(pageLayer);
    //                    }
    //                }
    //                booksPageService.save(bookPage);
    //            }
    //        }
    //        Books book = books.get();
    //        book.setBooksPages(booksPages);
    //        return booksService.update(book);
    //    }
}
