import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WriterToolsDetailComponent } from '../../../../../../main/webapp/app/entities/writer-tools/writer-tools-detail.component';
import { WriterToolsService } from '../../../../../../main/webapp/app/entities/writer-tools/writer-tools.service';
import { WriterTools } from '../../../../../../main/webapp/app/entities/writer-tools/writer-tools.model';

describe('Component Tests', () => {

    describe('WriterTools Management Detail Component', () => {
        let comp: WriterToolsDetailComponent;
        let fixture: ComponentFixture<WriterToolsDetailComponent>;
        let service: WriterToolsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [WriterToolsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WriterToolsService,
                    EventManager
                ]
            }).overrideTemplate(WriterToolsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WriterToolsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WriterToolsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WriterTools(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.writerTools).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
