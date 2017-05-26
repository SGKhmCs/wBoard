import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { WriterTools } from './writer-tools.model';
import { WriterToolsPopupService } from './writer-tools-popup.service';
import { WriterToolsService } from './writer-tools.service';
import { Writer, WriterService } from '../writer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-writer-tools-dialog',
    templateUrl: './writer-tools-dialog.component.html'
})
export class WriterToolsDialogComponent implements OnInit {

    writerTools: WriterTools;
    authorities: any[];
    isSaving: boolean;

    writers: Writer[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private writerToolsService: WriterToolsService,
        private writerService: WriterService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.writerService
            .query({filter: 'writertools-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.writerTools.writerId) {
                    this.writers = res.json;
                } else {
                    this.writerService
                        .find(this.writerTools.writerId)
                        .subscribe((subRes: Writer) => {
                            this.writers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.writerTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.writerToolsService.update(this.writerTools));
        } else {
            this.subscribeToSaveResponse(
                this.writerToolsService.create(this.writerTools));
        }
    }

    private subscribeToSaveResponse(result: Observable<WriterTools>) {
        result.subscribe((res: WriterTools) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WriterTools) {
        this.eventManager.broadcast({ name: 'writerToolsListModification', content: 'OK'});
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

    trackWriterById(index: number, item: Writer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-writer-tools-popup',
    template: ''
})
export class WriterToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private writerToolsPopupService: WriterToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.writerToolsPopupService
                    .open(WriterToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.writerToolsPopupService
                    .open(WriterToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
