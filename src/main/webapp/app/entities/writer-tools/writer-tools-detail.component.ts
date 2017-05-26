import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { WriterTools } from './writer-tools.model';
import { WriterToolsService } from './writer-tools.service';

@Component({
    selector: 'jhi-writer-tools-detail',
    templateUrl: './writer-tools-detail.component.html'
})
export class WriterToolsDetailComponent implements OnInit, OnDestroy {

    writerTools: WriterTools;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private writerToolsService: WriterToolsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWriterTools();
    }

    load(id) {
        this.writerToolsService.find(id).subscribe((writerTools) => {
            this.writerTools = writerTools;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWriterTools() {
        this.eventSubscriber = this.eventManager.subscribe(
            'writerToolsListModification',
            (response) => this.load(this.writerTools.id)
        );
    }
}
