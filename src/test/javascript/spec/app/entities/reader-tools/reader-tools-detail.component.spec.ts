import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReaderToolsDetailComponent } from '../../../../../../main/webapp/app/entities/reader-tools/reader-tools-detail.component';
import { ReaderToolsService } from '../../../../../../main/webapp/app/entities/reader-tools/reader-tools.service';
import { ReaderTools } from '../../../../../../main/webapp/app/entities/reader-tools/reader-tools.model';

describe('Component Tests', () => {

    describe('ReaderTools Management Detail Component', () => {
        let comp: ReaderToolsDetailComponent;
        let fixture: ComponentFixture<ReaderToolsDetailComponent>;
        let service: ReaderToolsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [ReaderToolsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReaderToolsService,
                    EventManager
                ]
            }).overrideComponent(ReaderToolsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReaderToolsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReaderToolsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ReaderTools(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.readerTools).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
