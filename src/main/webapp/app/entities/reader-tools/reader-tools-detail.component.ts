import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { ReaderTools } from './reader-tools.model';
import { ReaderToolsService } from './reader-tools.service';

@Component({
    selector: 'jhi-reader-tools-detail',
    templateUrl: './reader-tools-detail.component.html'
})
export class ReaderToolsDetailComponent implements OnInit, OnDestroy {

    readerTools: ReaderTools;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private readerToolsService: ReaderToolsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReaderTools();
    }

    load(id) {
        this.readerToolsService.find(id).subscribe((readerTools) => {
            this.readerTools = readerTools;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReaderTools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'readerToolsListModification',
            (response) => this.load(this.readerTools.id)
        );
    }
}
