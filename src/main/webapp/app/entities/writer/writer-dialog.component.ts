import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Writer } from './writer.model';
import { WriterPopupService } from './writer-popup.service';
import { WriterService } from './writer.service';
import { Board, BoardService } from '../board';
import { Reader, ReaderService } from '../reader';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-writer-dialog',
    templateUrl: './writer-dialog.component.html'
})
export class WriterDialogComponent implements OnInit {

    writer: Writer;
    authorities: any[];
    isSaving: boolean;

    boards: Board[];

    readers: Reader[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private writerService: WriterService,
        private boardService: BoardService,
        private readerService: ReaderService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.boardService.query()
            .subscribe((res: ResponseWrapper) => { this.boards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.readerService
            .query({filter: 'writer-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.writer.reader || !this.writer.reader.id) {
                    this.readers = res.json;
                } else {
                    this.readerService
                        .find(this.writer.reader.id)
                        .subscribe((subRes: Reader) => {
                            this.readers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.writer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.writerService.update(this.writer));
        } else {
            this.subscribeToSaveResponse(
                this.writerService.create(this.writer));
        }
    }

    private subscribeToSaveResponse(result: Observable<Writer>) {
        result.subscribe((res: Writer) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Writer) {
        this.eventManager.broadcast({ name: 'writerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackBoardById(index: number, item: Board) {
        return item.id;
    }

    trackReaderById(index: number, item: Reader) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-writer-popup',
    template: ''
})
export class WriterPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private writerPopupService: WriterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.writerPopupService
                    .open(WriterDialogComponent, params['id']);
            } else {
                this.modalRef = this.writerPopupService
                    .open(WriterDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
