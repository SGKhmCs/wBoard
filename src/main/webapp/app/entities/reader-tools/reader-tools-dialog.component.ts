import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { ReaderTools } from './reader-tools.model';
import { ReaderToolsPopupService } from './reader-tools-popup.service';
import { ReaderToolsService } from './reader-tools.service';
import { Reader, ReaderService } from '../reader';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-reader-tools-dialog',
    templateUrl: './reader-tools-dialog.component.html'
})
export class ReaderToolsDialogComponent implements OnInit {

    readerTools: ReaderTools;
    authorities: any[];
    isSaving: boolean;

    readers: Reader[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private readerToolsService: ReaderToolsService,
        private readerService: ReaderService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.readerService
            .query({filter: 'readertools-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.readerTools.readerId) {
                    this.readers = res.json;
                } else {
                    this.readerService
                        .find(this.readerTools.readerId)
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
        if (this.readerTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.readerToolsService.update(this.readerTools));
        } else {
            this.subscribeToSaveResponse(
                this.readerToolsService.create(this.readerTools));
        }
    }

    private subscribeToSaveResponse(result: Observable<ReaderTools>) {
        result.subscribe((res: ReaderTools) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ReaderTools) {
        this.eventManager.broadcast({ name: 'readerToolsListModification', content: 'OK'});
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

    trackReaderById(index: number, item: Reader) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-reader-tools-popup',
    template: ''
})
export class ReaderToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private readerToolsPopupService: ReaderToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.readerToolsPopupService
                    .open(ReaderToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.readerToolsPopupService
                    .open(ReaderToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
