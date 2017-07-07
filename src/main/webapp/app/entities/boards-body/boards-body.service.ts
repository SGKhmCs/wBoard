import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { BoardsBody } from './boards-body.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BoardsBodyService {

    private resourceUrl = 'api/boards-bodies';
    private resourceSearchUrl = 'api/_search/boards-bodies';

    constructor(private http: Http) { }

    create(boardsBody: BoardsBody): Observable<BoardsBody> {
        const copy = this.convert(boardsBody);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(boardsBody: BoardsBody): Observable<BoardsBody> {
        const copy = this.convert(boardsBody);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<BoardsBody> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(boardsBody: BoardsBody): BoardsBody {
        const copy: BoardsBody = Object.assign({}, boardsBody);
        return copy;
    }
}
